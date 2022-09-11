package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.UserItemEntity
import java.util.UUID

class UserItemEntityBuilder {
    var id: UUID = UUID.randomUUID()
    var name: String? = "item"
    var description: String? = "description"
    var weight: Int? = 100
    var publicVisibility: Boolean? = true
    var userId: UUID = UUID.randomUUID()

    fun id(id: UUID) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun description(description: String) = apply { this.description = description }
    fun weight(weight: Int) = apply { this.weight = weight }
    fun publicVisibility(publicVisibility: Boolean) = apply { this.publicVisibility = publicVisibility }
    fun userId(userId: UUID) = apply { this.userId = userId }

    fun build() = UserItemEntity(
        id,
        name,
        description,
        weight,
        publicVisibility,
        userId
    )
}
