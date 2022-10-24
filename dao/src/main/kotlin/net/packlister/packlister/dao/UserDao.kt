package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.UserEntity
import net.packlister.packlister.dao.model.NewUser
import net.packlister.packlister.dao.repository.UserRepository
import net.packlister.packlister.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class UserDao(
    @Autowired
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(newUser: NewUser): User {
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            username = newUser.username,
            email = newUser.email,
            passwordHash = newUser.passwordHash,
            active = true
        )

        userRepository.persist(userEntity)

        return userEntity.toModel()
    }

    fun getByUsername(username: String): User? {
        val user = userRepository.findByUsernameIgnoreCase(username)
        return if (user.isPresent) {
            user.get().toModel()
        } else {
            null
        }
    }
}
