package gre.application.entities.successor

import gre.application.entities.task.TaskEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object SuccessorEntity : Table("successor") {
	val id: Column<Int> = integer("id")
	val taskId: Column<EntityID<Int>> = reference("task_id", TaskEntity)
}