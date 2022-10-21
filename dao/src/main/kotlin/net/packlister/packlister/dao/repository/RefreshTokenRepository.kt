package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID>, HibernateRepository<RefreshTokenEntity> {
    fun findByFamily(family: UUID): Optional<RefreshTokenEntity>

    fun deleteByFamily(family: UUID): Int
}
