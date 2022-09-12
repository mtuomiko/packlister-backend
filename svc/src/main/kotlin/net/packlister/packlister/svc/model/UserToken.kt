package net.packlister.packlister.svc.model

data class UserToken(
    val token: String,
    val username: String,
    val email: String
)
