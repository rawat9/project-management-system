package gre.application.domain

/**
 * Data Access Object
 */
interface Dao<T> {
    fun getAll(): List<T>

    fun getById(id: Int): T?

    fun save(entity: T): Unit

    fun delete(id: Int): Unit

    fun create(entity: T): Int
}