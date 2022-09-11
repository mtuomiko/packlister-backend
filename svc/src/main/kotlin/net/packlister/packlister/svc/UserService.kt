package net.packlister.packlister.svc

import net.packlister.packlister.dao.UserDao
import net.packlister.packlister.dao.model.NewUser
import net.packlister.packlister.model.ConflictError
import net.packlister.packlister.model.User
import net.packlister.packlister.svc.model.UserRegistration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * Implements UserDetailsService, so is also used by security configuration.
 */
@Component
class UserService(
    @Autowired
    private val userDao: UserDao,
    @Autowired
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    fun register(userRegistration: UserRegistration): User {
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
        return userDao.getByUsername(username) ?: throw UsernameNotFoundException("$username not found")
    }
}
