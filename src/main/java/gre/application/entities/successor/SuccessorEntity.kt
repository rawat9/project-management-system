package gre.application.entities.successor

import gre.application.entities.task.TaskEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SuccessorEntity : Table("successor") {
	val id: Column<EntityID<Int>> = reference("id", TaskEntity, onDelete = ReferenceOption.CASCADE)
	val taskId: Column<EntityID<Int>> = reference("task_id", TaskEntity, onDelete = ReferenceOption.CASCADE)
}