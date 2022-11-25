package net.packlister.packlister.dao.entity

import net.packlister.packlister.model.UserItem
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "items")
@EntityListeners(AuditingEntityListener::class)
class UserItemEntity(
    @Id
    val id: UUID,
    val name: String?,
    val description: String?,
    val weight: Int?,
    val publicVisibility: Boolean = false,
    @Column(name = "account_id")
    val userId: UUID
) {
    @field:LastModifiedDate
    lateinit var modifiedAt: Instant

    @field:CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: Instant

    fun toModel() = with(this) { UserItem(id, name, description, weight, publicVisibility) }
}
