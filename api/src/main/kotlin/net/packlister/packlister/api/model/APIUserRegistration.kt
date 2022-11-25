package net.packlister.packlister.api.model

import jakarta.validation.constraints.Email

class APIUserRegistration(
    val username: String,
    @field:Email
    val email: String,
    val password: String
)
