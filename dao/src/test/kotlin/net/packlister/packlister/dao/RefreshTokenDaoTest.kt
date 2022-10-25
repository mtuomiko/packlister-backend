package net.packlister.packlister.dao

import net.packlister.packlister.dao.builders.RefreshTokenEntityBuilder
import net.packlister.packlister.dao.builders.UserEntityBuilder
import net.packlister.packlister.dao.repository.RefreshTokenRepository
import net.packlister.packlister.dao.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.Instant
import java.time.InstantSource
import java.time.temporal.ChronoUnit
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
class RefreshTokenDaoTest(
    @Autowired private val refreshTokenRepository: RefreshTokenRepository,
    @Autowired private val userRepository: UserRepository
) {
    val testInstant: Instant = Instant.parse("2022-10-25T21:44:58Z")
    val fixedInstantSource: InstantSource = InstantSource.fixed(testInstant)
    val refreshTokenDao = RefreshTokenDao(refreshTokenRepository, fixedInstantSource)

    val testUserId: UUID = UUID.fromString("7cbcf87e-5ad6-4d94-9df5-bc2bca372ec5")

    @BeforeEach
    fun testSetup() {
        userRepository.persistAndFlush(UserEntityBuilder().id(testUserId).build())
    }

    @Test
    fun deleteExpired_whenValidAndExpiredTokensExist_deletesOnlyExpiredTokens() {
        val expiredEntities = List(2) {
            RefreshTokenEntityBuilder()
                .userId(testUserId)
                .expiresAt(testInstant.minus(1, ChronoUnit.DAYS))
                .build()
        }
        val validEntities = List(2) {
            RefreshTokenEntityBuilder()
                .userId(testUserId)
                .expiresAt(testInstant.plus(1, ChronoUnit.DAYS))
                .build()
        }
        val allEntities = refreshTokenRepository.persistAllAndFlush(expiredEntities + validEntities)

        val result = refreshTokenDao.deleteExpired()

        val remainingTokens = refreshTokenRepository.findAllById(allEntities.map { it.id })
        assertThat(result).isEqualTo(expiredEntities.size)
        assertThat(remainingTokens).containsExactlyInAnyOrderElementsOf(validEntities)
    }
}
