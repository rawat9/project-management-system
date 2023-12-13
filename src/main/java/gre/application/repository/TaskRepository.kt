package gre.application.repository

import gre.application.entities.successor.Successor
import gre.application.entities.successor.SuccessorEntity
import gre.application.entities.task.Task
import gre.application.entities.task.TaskEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
open class TaskRepository : Repository<Task> {
	
	/**
	 * Returns list of all tasks
	 */
	override fun getAll(): List<Task> {
		return TaskEntity.selectAll().map {
			Task(
				id = it[TaskEntity.id].value,
				name = it[TaskEntity.name],
				description = it[TaskEntity.description],
				projectId = it[TaskEntity.projectId].value,
				deadline = it[TaskEntity.deadline],
				status = it[TaskEntity.status],
				priority = it[TaskEntity.priority],
				successors = getAllSuccessors(taskId = it[TaskEntity.id].value),
			)
		}
	}
	
	/**
	 * Returns a task by a given id or null if the task doesn't exist
	 */
	override fun getById(id: Int): Task? {
		return TaskEntity.select { TaskEntity.id eq id }.firstOrNull()?.let {
			Task(
				id = it[TaskEntity.id].value,
				name = it[TaskEntity.name],
				description = it[TaskEntity.description],
				projectId = it[TaskEntity.projectId].value,
				deadline = it[TaskEntity.deadline],
				status = it[TaskEntity.status],
				priority = it[TaskEntity.priority],
				successors = getAllSuccessors(taskId = it[TaskEntity.id].value),
			)
		}
	}
	
	/**
	 * Deletes a task with the given id from the database
	 */
	override fun delete(id: Int) {
		TaskEntity.deleteWhere { TaskEntity.id eq id }
	}
	
	/**
	 * Inserts a new record/row and return the ID
	 */
	override fun create(entity: Task): Int {
		val id = TaskEntity.insertAndGetId {
			it[name] = entity.name
			it[description] = entity.description
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
		}
		return id.value
	}
	
	/**
	 * Returns a list of all successors for a given taskID
	 */
	private fun getAllSuccessors(taskId: Int): Set<Int> {
		return SuccessorEntity.select { SuccessorEntity.taskId eq taskId }.map { it[SuccessorEntity.id].value }.toSet()
	}
	
	/**
	 * Deletes a successor with the given id and id of the task it is associated to
	 */
	fun removeSuccessor(successor: Successor) {
		transaction {
			SuccessorEntity.deleteWhere { id eq successor.id and (taskId eq successor.taskId) }
		}
	}
	
	/**
	 * Adds a new successor to the task
	 */
	fun addSuccessor(entity: Successor) {
		transaction {
			SuccessorEntity.insert {
				it[id] = entity.id
				it[taskId] = entity.taskId
			}
		}
	}
	
	/**
	 * Updates a task entity with the given task
	 */
	override fun update(entity: Task) {
		TaskEntity.update({ TaskEntity.id eq entity.id }) {
			it[name] = entity.name
			it[description] = entity.description
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
		}
	}
}
