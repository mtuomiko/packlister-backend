package net.packlister.packlister.svc.model

import java.util.UUID

data class TokensWithUserInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: UUID,
    val username: String,
    val email: String
)
