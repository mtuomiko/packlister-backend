package net.packlister.packlister.api.model

import java.util.UUID

class APIUser(
    val id: UUID,
    val username: String,
    val email: String
)
