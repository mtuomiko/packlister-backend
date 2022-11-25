package net.packlister.packlister.svc.model

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import net.packlister.packlister.svc.validation.Validators.alphaNumericRegexString
import net.packlister.packlister.svc.validation.Validators.simpleEmailRegexString


class UserRegistration(
    @field:Pattern(
        regexp = alphaNumericRegexString,
        message = "Only alphanumeric characters and . _ - allowed for username"
    )
    @field:Size(min = 3, message = "Username must have at least three (3) characters")
    val username: String,
    @field:Pattern(message = "Check that email format follows pattern xxx@yyy.zzz", regexp = simpleEmailRegexString)
    val email: String,
    @field:Size(message = "Password must have at least six (6) characters", min = 6)
    val password: String
)
