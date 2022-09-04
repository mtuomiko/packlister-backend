package net.packlister.packlister.dao.repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class HibernateRepositoryImpl<T>(
    @PersistenceContext
    private val entityManager: EntityManager
) : HibernateRepository<T> {
    override fun <S : T> persist(entity: S): S {
        entityManager.persist(entity)
        return entity
    }

    override fun <S : T> persistAll(entities: Iterable<S>) {
        TODO("Not yet implemented")
    }
}
