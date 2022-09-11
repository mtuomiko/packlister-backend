package net.packlister.packlister.dao.entity

import net.packlister.packlister.model.User
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
class UserEntity(
    @Id
    val id: UUID,
    val username: String,
    val email: String,
    val passwordHash: String
) {
    fun toModel() = with(this) { User(id, username, email, passwordHash) }
}
