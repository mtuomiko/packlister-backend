package net.packlister.packlister.dao.builders

import net.packlister.packlister.dao.entity.RefreshTokenEntity
import java.time.Instant
import java.util.UUID

class RefreshTokenEntityBuilder {
    var id: UUID = UUID.randomUUID()
    var token: String = "foobar"
    var family: UUID = UUID.randomUUID()
    var expiresAt: Instant = Instant.parse("2022-10-25T21:44:58Z")
    var userId: UUID = UUID.randomUUID()

    fun id(id: UUID) = apply { this.id = id }
    fun token(token: String) = apply { this.token = token }
    fun family(family: UUID) = apply { this.family = family }
    fun expiresAt(expiresAt: Instant) = apply { this.expiresAt = expiresAt }
    fun userId(userId: UUID) = apply { this.userId = userId }

    fun build() = RefreshTokenEntity(
        id,
        token,
        family,
        expiresAt,
        userId
    )
}
