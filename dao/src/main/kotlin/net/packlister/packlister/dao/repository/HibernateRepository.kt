package net.packlister.packlister.dao.repository

interface HibernateRepository<T> {
    fun <S : T> persist(entity: S): S

    fun <S : T> persistAll(entities: Iterable<S>)
}
