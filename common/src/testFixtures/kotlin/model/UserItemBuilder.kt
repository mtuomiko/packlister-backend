package model

import net.packlister.packlister.model.UserItem
import java.util.UUID

class UserItemBuilder {
    var id: UUID = UUID.randomUUID()
    var name: String? = "item"
    var description: String? = "description"
    var weight: Int? = 100
    var publicVisibility: Boolean? = true

    fun id(id: UUID) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun description(description: String) = apply { this.description = description }
    fun weight(weight: Int) = apply { this.weight = weight }
    fun publicVisibility(publicVisibility: Boolean) = apply { this.publicVisibility = publicVisibility }

    fun build() = UserItem(
        id,
        name,
        description,
        weight,
        publicVisibility
    )
}
