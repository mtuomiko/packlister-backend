package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.UserItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserItemRepository : JpaRepository<UserItemEntity, UUID>, HibernateRepository<UserItemEntity>
