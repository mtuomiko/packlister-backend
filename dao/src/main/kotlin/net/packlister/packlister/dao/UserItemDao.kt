package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.UserItemEntity
import net.packlister.packlister.dao.repository.UserItemRepository
import net.packlister.packlister.model.UserItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserItemDao(
    @Autowired
    private val userItemRepository: UserItemRepository
) {
    fun upsertUserItems(userItems: List<UserItem>): List<UserItem> {
        val userItemEntities = userItems.map { it.toEntity() }
        val savedUserItems = userItemRepository.saveAll(userItemEntities)
        return savedUserItems.map { it.toModel() }
    }

    private fun UserItem.toEntity() = with(this) {
        UserItemEntity(
            id,
            name,
            description,
            weight,
            publicVisibility,
            UUID.randomUUID()
        )
    }
}
