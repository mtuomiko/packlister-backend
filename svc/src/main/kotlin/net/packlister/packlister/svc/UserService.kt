package net.packlister.packlister.svc

import mu.KotlinLogging
import net.packlister.packlister.dao.UserDao
import net.packlister.packlister.dao.model.NewUser
import net.packlister.packlister.model.ConflictError
import net.packlister.packlister.model.InnerError
import net.packlister.packlister.model.User
import net.packlister.packlister.model.ValidationError
import net.packlister.packlister.svc.model.UserRegistration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.validation.Validator

private val logger = KotlinLogging.logger {}

/**
 * Implements UserDetailsService, so is also used by security configuration.
 */
@Component
class UserService(
    @Autowired
    private val userDao: UserDao,
    @Autowired
    private val passwordEncoder: PasswordEncoder,
    @Autowired
    private val validator: Validator
) : UserDetailsService {
    fun register(userRegistration: UserRegistration): User {
        val violations = validator.validate(userRegistration)
        if (violations.isNotEmpty()) {
            val errors = violations.map { InnerError(message = it.message, target = it.propertyPath.last().name) }
            throw ValidationError(innerErrors = errors)
        }
        val user = try {
            userDao.create(
                NewUser(
                    username = userRegistration.username,
                    email = userRegistration.email,
                    passwordHash = passwordEncoder.encode(userRegistration.password)
                )
            )
        } catch (err: DataIntegrityViolationException) {
            throw ConflictError("username or email already in use", err)
        }
        return user
    }

    fun getUser(username: String): User? = userDao.getByUsername(username)

    override fun loadUserByUsername(username: String): User {
        val user = userDao.getByUsername(username)
        if (user == null) {
            logger.info { "failed to load user by username $username" }
            throw UsernameNotFoundException("username not found")
        }
        return user
    }
}
