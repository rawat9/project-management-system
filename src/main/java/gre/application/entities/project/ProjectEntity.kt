package gre.application.entities.project

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object ProjectEntity : IntIdTable("project") {
	val name: Column<String> = varchar("name", 100)
	val description: Column<String?> = largeText("description").nullable()
	val createdDate: Column<LocalDate> = date("created_date").clientDefault { LocalDate.now() }
}
