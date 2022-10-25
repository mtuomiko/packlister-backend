package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID>, HibernateRepository<RefreshTokenEntity> {
    fun findByFamily(family: UUID): RefreshTokenEntity?

    fun deleteByFamily(family: UUID): Int

    fun findByExpiresAtIsBefore(threshold: Instant): List<RefreshTokenEntity>

    /**
     * Bulk delete for expired tokens. Does bypass relevant JPA lifecycle methods (pre and post delete), but they are
     * not currently used so non-issue at the moment.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} token WHERE token.id IN ?1")
    fun bulkDeleteWithIds(ids: List<UUID>): Int
}
