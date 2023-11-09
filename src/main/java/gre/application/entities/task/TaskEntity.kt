package gre.application.entities.task

import gre.application.entities.project.ProjectEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object TaskEntity : IntIdTable("task") {
	val projectId: Column<EntityID<Int>> = reference("project_id", ProjectEntity)
	val taskName: Column<String> = varchar("task_name", 100)
	val taskDescription: Column<String> = largeText("task_description")
	val status: Column<String> = varchar("status", 10)
	val priority: Column<String> = varchar("priority", 10)
	val successorId: Column<Int?> = integer("successor_id").nullable()
	val createdDate: Column<LocalDate> = date("created_date").clientDefault { LocalDate.now() }
	val deadline: Column<LocalDate> = date("deadline")
}
