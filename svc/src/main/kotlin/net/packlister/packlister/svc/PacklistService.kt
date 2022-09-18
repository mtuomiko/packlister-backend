package net.packlister.packlister.svc

import net.packlister.packlister.TokenReader
import net.packlister.packlister.dao.PacklistDao
import net.packlister.packlister.model.NotFoundError
import net.packlister.packlister.model.Packlist
import net.packlister.packlister.model.PacklistLimited
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PacklistService(
    @Autowired
    private val packlistDao: PacklistDao,
    @Autowired
    private val tokenReader: TokenReader
) {
    fun getPacklists(): List<PacklistLimited> {
        return packlistDao.getUserPacklists(tokenReader.getUserId())
    }

    fun getOnePacklist(id: UUID): Packlist {
        return packlistDao.getOnePacklist(tokenReader.getUserId(), id)
            ?: throw NotFoundError("packlist with id $id not found")
    }

    fun createPacklist(packlist: Packlist): Packlist {
        return packlistDao.createPacklist(tokenReader.getUserId(), packlist)
    }

    fun updatePacklist(packlist: Packlist): Packlist {
        return packlistDao.updatePacklist(tokenReader.getUserId(), packlist)
    }
}
