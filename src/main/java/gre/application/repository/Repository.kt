package gre.application.repository

/**
 * Generic CRUD Repository
 */
interface Repository<T> {
	fun getAll(): List<T>
	
	fun getById(id: Int): T?
	
	fun update(id: Int, entity: T): Unit
	
	fun delete(id: Int): Unit
	
	fun create(entity: T): Int
}