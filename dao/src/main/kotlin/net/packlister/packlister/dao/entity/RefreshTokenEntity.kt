package net.packlister.packlister.dao.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.packlister.packlister.dao.model.RefreshToken
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
@EntityListeners(AuditingEntityListener::class)
class RefreshTokenEntity(
    @Id
    val id: UUID,
    val token: String,
    val family: UUID,
    @Column(name = "expires_at")
    val expiresAt: Instant,
    @Column(name = "account_id")
    val userId: UUID
) {
    @field:LastModifiedDate
    lateinit var modifiedAt: Instant

    @field:CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: Instant

    fun toModel() = with(this) { RefreshToken(id, token, family, expiresAt, userId) }
}
