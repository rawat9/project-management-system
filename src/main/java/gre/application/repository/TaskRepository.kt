package gre.application.repository

import gre.application.entities.task.Task
import gre.application.entities.task.TaskEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
open class TaskRepository : Repository<Task> {
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
				successorId = it[TaskEntity.successorId]
			)
		}
	}
	
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
				successorId = it[TaskEntity.successorId]
			)
		}
	}
	
	override fun delete(id: Int) {
		TaskEntity.deleteWhere { TaskEntity.id eq id }
	}
	
	override fun create(entity: Task): Int {
		val id = TaskEntity.insertAndGetId {
			it[name] = entity.name
			it[description] = entity.description
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
			it[successorId] = entity.successorId
		}
		
		return id.value
	}
	
	override fun update(entity: Task) {
		TaskEntity.update({ TaskEntity.id eq entity.id }) {
			it[name] = entity.name
			it[description] = entity.description
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
			it[successorId] = entity.successorId
		}
	}
}