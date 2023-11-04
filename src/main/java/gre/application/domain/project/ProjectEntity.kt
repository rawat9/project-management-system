package gre.application.domain.project

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object ProjectEntity : IntIdTable("project") {
    val projectName: Column<String> = varchar("project_name", 100)
    val projectDescription: Column<String> = largeText("project_description")
    val createdDate: Column<LocalDate> = date("created_date").clientDefault { LocalDate.now() }
}
