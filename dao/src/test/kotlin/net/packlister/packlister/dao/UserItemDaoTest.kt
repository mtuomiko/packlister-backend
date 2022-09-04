package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.AccountEntity
import net.packlister.packlister.dao.entity.UserItemEntity
import net.packlister.packlister.dao.repository.AccountRepository
import net.packlister.packlister.dao.repository.UserItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserItemDaoTest(
    @Autowired
    private val userItemRepository: UserItemRepository,
    @Autowired
    private val accountRepository: AccountRepository
) {
    val testId: UUID = UUID.fromString("30b5ac32-7fb8-4877-9b31-685bc8232c90")

    @BeforeEach
    fun addUser() {
        accountRepository.persist(
            AccountEntity(
                id = testId,
                username = "Holoppa",
                email = "mail"
            )
        )
    }

    @Test
    fun canRead() {
        assertThat(accountRepository.findById(testId).isPresent).isTrue
    }

    @Test
    fun canSave() {
        val entity = UserItemEntity(
            id = UUID.randomUUID(),
            name = "foo",
            description = "blaah",
            weight = 500,
            publicVisibility = true,
            account = testId
        )

        userItemRepository.persist(entity)

        val storedEntity = userItemRepository.findById(entity.id).get()
        assertThat(storedEntity).isEqualTo(entity)

        val otherEntity = userItemRepository.findById(UUID.randomUUID())
        assertThat(otherEntity.isEmpty).isTrue
    }
}
