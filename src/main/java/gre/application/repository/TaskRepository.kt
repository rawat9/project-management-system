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
				taskId = it[TaskEntity.id].value,
				taskName = it[TaskEntity.taskName],
				taskDescription = it[TaskEntity.taskDescription],
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
				taskId = it[TaskEntity.id].value,
				taskName = it[TaskEntity.taskName],
				taskDescription = it[TaskEntity.taskDescription],
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
			it[taskName] = entity.taskName
			it[taskDescription] = entity.taskDescription
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
			it[successorId] = entity.successorId
		}
		
		return id.value
	}
	
	override fun update(id: Int, entity: Task) {
		TaskEntity.update({ TaskEntity.id eq id }) {
			it[taskName] = entity.taskName
			it[taskDescription] = entity.taskDescription
			it[projectId] = entity.projectId
			it[deadline] = entity.deadline
			it[status] = entity.status
			it[priority] = entity.priority
			it[successorId] = entity.successorId
		}
	}
}