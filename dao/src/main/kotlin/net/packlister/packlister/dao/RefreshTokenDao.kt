package net.packlister.packlister.dao

import mu.KotlinLogging
import net.packlister.packlister.dao.entity.RefreshTokenEntity
import net.packlister.packlister.dao.model.RefreshToken
import net.packlister.packlister.dao.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.InstantSource
import java.util.UUID
import javax.transaction.Transactional

private val logger = KotlinLogging.logger {}

@Component
class RefreshTokenDao(
    @Autowired private val refreshTokenRepository: RefreshTokenRepository,
    @Autowired private val instantSource: InstantSource
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
        return refreshTokenRepository.findByFamily(tokenFamily)?.toModel()
    }

    fun deleteById(id: UUID) {
        refreshTokenRepository.deleteById(id)
    }

    @Transactional
    fun deleteByTokenFamily(tokenFamily: UUID) {
        refreshTokenRepository.deleteByFamily(tokenFamily)
    }

    /**
     * Clear expired refresh tokens from database every night 02:35 server time. Should have no security impact, expired
     * tokens should be treated practically the same as non-existing ones.
     */

    @Transactional
    fun deleteExpired(): Int {
        val expiredTokens = refreshTokenRepository.findByExpiresAtIsBefore(instantSource.instant())
        return if (expiredTokens.isNotEmpty()) {
            refreshTokenRepository.bulkDeleteWithIds(expiredTokens.map { it.id })
        } else {
            0
        }
    }
}
