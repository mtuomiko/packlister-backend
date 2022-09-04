package net.packlister.packlister.dao.repository

import net.packlister.packlister.dao.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccountRepository : JpaRepository<AccountEntity, UUID>, HibernateRepository<AccountEntity>
