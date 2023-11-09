package gre.application.entities.task

import gre.application.entities.project.ProjectEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object TaskEntity : IntIdTable("task") {
	val projectId: Column<EntityID<Int>> = reference("project_id", ProjectEntity)
	val name: Column<String> = varchar("name", 100)
	val description: Column<String> = largeText("description")
	val status: Column<Status> = enumeration("status")
	val priority: Column<Priority> = enumeration("priority")
	val successorId: Column<Int?> = integer("successor_id").nullable()
	val createdDate: Column<LocalDate> = date("created_date").clientDefault { LocalDate.now() }
	val deadline: Column<LocalDate> = date("deadline")
}
