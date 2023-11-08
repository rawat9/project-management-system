package gre.application.repository

import gre.application.entities.project.Project
import gre.application.entities.project.ProjectEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
open class ProjectRepository : Repository<Project> {
	
	/**
	 * Returns list of all projects
	 */
	override fun getAll(): List<Project> {
		return ProjectEntity.selectAll().map {
			Project(
				projectId = it[ProjectEntity.id].value,
				projectName = it[ProjectEntity.projectName],
				projectDescription = it[ProjectEntity.projectDescription]
			)
		}
	}
	
	/**
	 * Insert a new record/row and return the ID
	 */
	override fun create(entity: Project): Int {
		val id = ProjectEntity.insertAndGetId {
			it[projectName] = entity.projectName
			it[projectDescription] = entity.projectDescription.orEmpty()
		}
		return id.value
	}
	
	/**
	 * Returns a project by a given id or null if the project doesn't exist
	 */
	override fun getById(id: Int): Project? {
		return ProjectEntity.select { ProjectEntity.id eq id }.firstOrNull()?.let {
			Project(
				projectId = it[ProjectEntity.id].value,
				projectName = it[ProjectEntity.projectName],
				projectDescription = it[ProjectEntity.projectDescription]
			)
		}
	}
	
	/**
	 * Deletes a project with the given id from the database
	 */
	override fun delete(id: Int) {
		ProjectEntity.deleteWhere { ProjectEntity.id eq id }
	}
	
	/**
	 * Updates a project entity with the given ID
	 */
	override fun update(id: Int, entity: Project) {
		ProjectEntity.update({ ProjectEntity.id eq id }) {
			it[projectName] = entity.projectName
			it[projectDescription] = entity.projectDescription.orEmpty()
		}
	}
}
