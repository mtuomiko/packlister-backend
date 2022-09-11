package net.packlister.packlister.dao.repository

import org.hibernate.Session

interface HibernateRepository<T> {
    fun <S : T> persist(entity: S): S

    fun <S : T> persistAndFlush(entity: S): S

    fun <S : T> persistAll(entities: Iterable<S>): List<S>

    fun <S : T> persistAllAndFlush(entities: Iterable<S>): List<S>

    fun <S : T> update(entity: S): S

    fun <S : T> updateAll(entities: Iterable<S>): List<S>

    fun session(): Session
}
