package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID>, HibernateRepository<UserEntity> {
    fun findByUsernameIgnoreCase(username: String): Optional<UserEntity>
}
