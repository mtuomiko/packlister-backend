package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.UserItemEntity
import net.packlister.packlister.dao.repository.UserItemRepository
import net.packlister.packlister.model.ForbiddenError
import net.packlister.packlister.model.UserItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class UserItemDao(
    @Autowired
    private val userItemRepository: UserItemRepository
) {
    fun getUserItems(userId: UUID): List<UserItem> {
        val userItems = userItemRepository.findAllByUserId(userId)
        return userItems.map { it.toModel() }
    }

    @Transactional
    fun upsertUserItems(userItems: List<UserItem>, userId: UUID): List<UserItem> {
        val itemIds = userItems.map { it.id }
        val existingItems: List<UserItemEntity> = userItemRepository.findAllById(itemIds)
        if (existingItems.any { it.userId != userId }) {
            throw ForbiddenError("not allowed to modify items of other users")
        }

        val existingItemIds = existingItems.map { it.id }
        val userItemEntities = userItems.map { it.toEntity(userId) }
        val (itemsToUpdate, itemsToCreate) = userItemEntities.partition { existingItemIds.contains(it.id) }

        userItemRepository.session().clear() // existing old entities in memory don't need to be managed
        val updatedItems = userItemRepository.updateAll(itemsToUpdate)
        val createdItems = userItemRepository.persistAll(itemsToCreate)
        val modifiedItems = updatedItems + createdItems
        return modifiedItems.map { it.toModel() }
    }

    @Transactional
    fun deleteUserItems(userItemIds: List<UUID>, userId: UUID) {
        val existingItems: List<UserItemEntity> = userItemRepository.findAllById(userItemIds)
        if (existingItems.any { it.userId != userId }) {
            throw ForbiddenError("not allowed to modify items of other users")
        }
        userItemRepository.deleteAll(existingItems)
    }

    private fun UserItem.toEntity(userId: UUID) = with(this) {
        UserItemEntity(
            id,
            name,
            description,
            weight,
            publicVisibility,
            userId
        )
    }
}
