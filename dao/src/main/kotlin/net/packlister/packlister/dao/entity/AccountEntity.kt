package net.packlister.packlister.dao.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Id
    val id: UUID,
    val username: String,
    val email: String
)
