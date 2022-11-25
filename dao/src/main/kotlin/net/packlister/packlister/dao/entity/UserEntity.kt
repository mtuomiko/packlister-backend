package net.packlister.packlister.dao.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.packlister.packlister.model.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Id
    val id: UUID,
    val username: String,
    val email: String,
    val passwordHash: String,
    val active: Boolean
) {
    @field:LastModifiedDate
    lateinit var modifiedAt: Instant

    @field:CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: Instant

    fun toModel() = with(this) { User(id, username, email, passwordHash, active) }
}
