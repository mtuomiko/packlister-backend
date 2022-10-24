package net.packlister.packlister.svc.model

import net.packlister.packlister.svc.validation.Validators.alphaNumericRegexString
import net.packlister.packlister.svc.validation.Validators.simpleEmailRegexString
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class UserRegistration(
    @field:Pattern(regexp = alphaNumericRegexString, message = "Only alphanumeric characters allowed for username")
    @field:Size(min = 3, message = "Username must have at least three (3) characters")
    val username: String,
    @field:Pattern(message = "Check that email format follows pattern xxx@yyy.zzz", regexp = simpleEmailRegexString)
    val email: String,
    @field:Size(message = "Password must have at least six (6) characters", min = 6)
    val password: String
)
