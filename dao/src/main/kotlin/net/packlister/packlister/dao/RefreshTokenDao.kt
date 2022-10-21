package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.RefreshTokenEntity
import net.packlister.packlister.dao.model.RefreshToken
import net.packlister.packlister.dao.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID
import javax.transaction.Transactional

@Component
class RefreshTokenDao(
    @Autowired
    private val refreshTokenRepository: RefreshTokenRepository
) {
    @Transactional
    fun create(refreshToken: RefreshToken) {
        val entity = RefreshTokenEntity(
            id = refreshToken.id,
            token = refreshToken.token,
            family = refreshToken.family,
            expiresAt = refreshToken.expiresAt,
            userId = refreshToken.userId
        )

        refreshTokenRepository.persist(entity)
    }

    fun getByTokenFamily(tokenFamily: UUID): RefreshToken? {
        val entityOpt = refreshTokenRepository.findByFamily(tokenFamily)
        if (entityOpt.isEmpty) return null

        return entityOpt.get().toModel()
    }

    fun deleteById(id: UUID) {
        refreshTokenRepository.deleteById(id)
    }

    @Transactional
    fun deleteByTokenFamily(tokenFamily: UUID) {
        refreshTokenRepository.deleteByFamily(tokenFamily)
    }
}
