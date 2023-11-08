package gre.application.entities.task

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TaskEntity : IntIdTable("task") {
	val taskName: Column<String> = varchar("task_name", 100)
	val taskDescription: Column<String> = largeText("task_description")
}
