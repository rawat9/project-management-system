package gre.application

import gre.application.entities.project.ProjectEntity
import gre.application.entities.successor.SuccessorEntity
import gre.application.entities.task.TaskEntity
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
object SchemaInitializer : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		transaction {
			SchemaUtils.createMissingTablesAndColumns(ProjectEntity, TaskEntity, SuccessorEntity)
		}
	}
}
