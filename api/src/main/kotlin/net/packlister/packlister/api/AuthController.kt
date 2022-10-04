package net.packlister.packlister.api

import net.packlister.packlister.generated.api.AuthApiDelegate
import net.packlister.packlister.generated.model.User
import net.packlister.packlister.svc.UserService
import net.packlister.packlister.svc.model.UserRegistration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import net.packlister.packlister.generated.model.UserRegistration as APIUserRegistration

@RestController
class AuthController(
    @Autowired
    private val userService: UserService
) : AuthApiDelegate {
    override fun register(userRegistration: APIUserRegistration): ResponseEntity<User> {
        val user = userService.register(
            UserRegistration(
                username = userRegistration.username,
                email = userRegistration.email,
                password = userRegistration.password
            )
        )
        val response = User().apply {
            id = user.id
            username = user.username
            email = user.email
        }
        return ResponseEntity.ok(response)
    }
}
