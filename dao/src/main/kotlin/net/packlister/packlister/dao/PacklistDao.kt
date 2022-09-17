package net.packlister.packlister.dao

import net.packlister.packlister.dao.entity.PacklistEntity
import net.packlister.packlister.dao.repository.PacklistRepository
import net.packlister.packlister.model.ForbiddenError
import net.packlister.packlister.model.Packlist
import net.packlister.packlister.model.PacklistLimited
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class PacklistDao(
    @Autowired
    private val packlistRepository: PacklistRepository
) {
    fun getUserPacklists(userId: UUID): List<PacklistLimited> {
        val packlistProjections = packlistRepository.findAllByUserId(userId)
        val packlists = packlistProjections.map {
            PacklistLimited(it.id, it.name)
        }
        return packlists
    }

    fun getOnePacklist(userId: UUID, packlistId: UUID): Packlist? {
        val packlistOpt = packlistRepository.findById(packlistId)
        if (packlistOpt.isEmpty) {
            return null
        }

        val packlist = packlistOpt.get()
        if (packlist.userId != userId) {
            throw ForbiddenError("not allowed to access packlists of other users")
        }
        return Packlist(
            id = packlist.id,
            name = packlist.name,
            description = packlist.description,
            categories = packlist.categories
        )
    }

    @Transactional
    fun upsertPacklist(userId: UUID, packlist: Packlist): Packlist {
        val entity = packlist.toEntity(userId)
        val existingPacklist = packlistRepository.findById(packlist.id)
        if (existingPacklist.isPresent) {
            return packlistRepository.update(entity).toModel()
        }
        return packlistRepository.persist(entity).toModel()
    }

    private fun Packlist.toEntity(userId: UUID) = with(this) {
        PacklistEntity(id, name, description, categories, userId)
    }
}
