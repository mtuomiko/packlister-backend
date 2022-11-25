package net.packlister.packlister.api.model

import jakarta.validation.constraints.NotEmpty

class APIUserCredentials(
    @field:NotEmpty
    val username: String,
    @field:NotEmpty
    val password: String
)
