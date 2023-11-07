package gre.application

import gre.application.domain.project.ProjectEntity
import gre.application.domain.task.TaskEntity
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
open class SchemaInitializer : ApplicationRunner {
	
	override fun run(args: ApplicationArguments?) {
		SchemaUtils.createMissingTablesAndColumns(ProjectEntity, TaskEntity)
	}
}
