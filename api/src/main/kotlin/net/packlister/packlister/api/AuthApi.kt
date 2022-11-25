package net.packlister.packlister.api

import jakarta.validation.Valid
import net.packlister.packlister.api.model.APITokenResponse
import net.packlister.packlister.api.model.APIUserCredentials
import net.packlister.packlister.api.model.APIUserRegistration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/auth")
interface AuthApi {

    @PostMapping("/register")
    fun register(@Valid @RequestBody userRegistration: APIUserRegistration): ResponseEntity<APITokenResponse>

    @PostMapping("/token")
    fun token(
        @CookieValue(name = "refresh_token", required = false) refreshToken: String?,
        @Valid @RequestBody(required = false) userCredentials: APIUserCredentials?
    ): ResponseEntity<APITokenResponse>

    @PostMapping("/logout")
    fun logout(@CookieValue(name = "refresh_token") refreshToken: String): ResponseEntity<Void>
}
