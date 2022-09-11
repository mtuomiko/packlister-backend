package net.packlister.packlister.dao.model

data class NewUser(
    val username: String,
    val email: String,
    val passwordHash: String
)
