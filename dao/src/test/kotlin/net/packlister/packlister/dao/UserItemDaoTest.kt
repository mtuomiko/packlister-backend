package net.packlister.packlister.dao

import model.UserItemBuilder
import net.packlister.packlister.dao.entity.UserEntity
import net.packlister.packlister.dao.entity.UserItemEntity
import net.packlister.packlister.dao.repository.UserItemRepository
import net.packlister.packlister.dao.repository.UserRepository
import net.packlister.packlister.model.ForbiddenError
import net.packlister.packlister.model.UserItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserItemDaoTest(
    @Autowired
    private val userItemRepository: UserItemRepository,
    @Autowired
    private val userRepository: UserRepository
) {
    val userItemDao = UserItemDao(userItemRepository)
    val testUserId1: UUID = UUID.fromString("30b5ac32-7fb8-4877-9b31-685bc8232c90")
    val testUserId2: UUID = UUID.fromString("30b5ac32-7fb8-4877-9b31-685bc8232c91")

    @BeforeEach
    fun testSetup() {
        val ids = listOf(testUserId1, testUserId2)
        userRepository.persistAllAndFlush(
            ids.mapIndexed { i, id ->
                UserEntity(id = id, username = "user$i", email = "user$i@mail.com", passwordHash = "hash")
            }
        )
    }

    @Test
    fun userExists_SavingNewItemsForUser_Succeeds() {
        val items = List(3) { UserItemBuilder().build() }

        userItemDao.upsertUserItems(items, testUserId1)

        val storedEntities = userItemRepository.findAllById(items.map { it.id })
        assertThat(itemListAndEntityListAreEquivalent(items, storedEntities, testUserId1)).isTrue
    }

    @Test
    fun userDoesNotExist_savingNewItemsForUser_throws() {
        val items = List(3) { UserItemBuilder().build() }
        userItemDao.upsertUserItems(items, UUID.randomUUID())
        val thrown = catchThrowable {
            userItemRepository.flush()
        }
        assertThat(thrown)
            .isInstanceOf(DataIntegrityViolationException::class.java)
            .hasMessageContaining("constraint")
    }

    @Test
    fun itemsExist_UpdatingItemsForUser_Succeeds() {
        val originalEntities = List(3) {
            UserItemEntityBuilder().name("old$it").userId(testUserId1).build()
        }
        userItemRepository.persistAllAndFlush(originalEntities)

        val items = originalEntities.map { UserItemBuilder().id(it.id).build() }
        userItemDao.upsertUserItems(items, testUserId1)

        val newEntities = userItemRepository.findAllById(items.map { it.id })
        assertThat(itemListAndEntityListAreEquivalent(items, newEntities, testUserId1)).isTrue
    }

    @Test
    fun itemsExistForOtherUser_UpdatingItems_ThrowsForbidden() {
        val originalEntities = List(3) {
            UserItemEntityBuilder().userId(testUserId2).build()
        }
        userItemRepository.persistAllAndFlush(originalEntities)

        val items = originalEntities.map { UserItemBuilder().id(it.id).build() }
        val thrown = catchThrowable {
            userItemDao.upsertUserItems(items, testUserId1)
        }

        val entities = userItemRepository.findAllById(originalEntities.map { it.id })
        assertThat(entities).containsExactlyInAnyOrderElementsOf(originalEntities)
        assertThat(thrown)
            .isInstanceOf(ForbiddenError::class.java)
            .hasMessageContaining("not allowed to modify items of other users")
    }

    @Test
    fun itemsExistForUser_DeletingItems_Succeeds() {
        val originalEntities = List(3) {
            UserItemEntityBuilder().userId(testUserId1).build()
        }
        userItemRepository.persistAllAndFlush(originalEntities)

        val ids = originalEntities.map { it.id }
        userItemDao.deleteUserItems(ids, testUserId1)

        val entities = userItemRepository.findAllById(originalEntities.map { it.id })
        assertThat(entities).isEmpty()
    }

    @Test
    fun itemsExistForOtherUser_DeletingItems_ThrowsForbidden() {
        val originalEntities = List(3) {
            UserItemEntityBuilder().name("old$it").userId(testUserId2).build()
        }
        userItemRepository.persistAllAndFlush(originalEntities)

        val ids = originalEntities.map { it.id }
        val thrown = catchThrowable {
            userItemDao.deleteUserItems(ids, testUserId1)
        }

        val entities = userItemRepository.findAllById(originalEntities.map { it.id })
        assertThat(entities).containsExactlyInAnyOrderElementsOf(originalEntities)
        assertThat(thrown)
            .isInstanceOf(ForbiddenError::class.java)
            .hasMessageContaining("not allowed to modify items of other users")
    }

    private fun UserItem.equalToEntityWithUserId(entity: UserItemEntity, userId: UUID): Boolean {
        return entity.id == this.id && entity.name == this.name && entity.description == this.description &&
            entity.weight == this.weight && entity.publicVisibility == this.publicVisibility &&
            entity.userId == userId
    }

    private fun itemListAndEntityListAreEquivalent(
        items: List<UserItem>,
        entities: List<UserItemEntity>,
        userId: UUID
    ): Boolean {
        val itemEntityPairList = items.flatMap { item ->
            entities.mapNotNull { entity ->
                if (item.id == entity.id) {
                    item to entity
                } else {
                    null
                }
            }
        }
        val isSameSize = items.size == entities.size && entities.size == itemEntityPairList.size
        return isSameSize && itemEntityPairList.all { it.first.equalToEntityWithUserId(it.second, userId) }
    }
}
