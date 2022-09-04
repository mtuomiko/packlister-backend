package net.packlister.packlister.dao.entity

import net.packlister.packlister.model.UserItem
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "items")
class UserItemEntity(
    @Id
    val id: UUID,
    val name: String?,
    val description: String?,
    val weight: Int?,
    val publicVisibility: Boolean? = false,
    val account: UUID?
) {
    fun toModel() = with(this) { UserItem(id, name, description, weight, publicVisibility) }
}
