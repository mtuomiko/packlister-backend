package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.PacklistEntity
import net.packlister.packlister.dao.projection.PacklistLimitedView
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PacklistRepository : JpaRepository<PacklistEntity, UUID>, HibernateRepository<PacklistEntity> {
    fun findAllByUserId(userId: UUID): List<PacklistLimitedView>
}
