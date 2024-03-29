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
				id = it[ProjectEntity.id].value,
				name = it[ProjectEntity.name],
				description = it[ProjectEntity.description]
			)
		}
	}
	
	/**
	 * Insert a new record/row and return the ID
	 */
	override fun create(entity: Project): Int {
		val id = ProjectEntity.insertAndGetId {
			it[name] = entity.name
			it[description] = entity.description.orEmpty()
		}
		return id.value
	}
	
	/**
	 * Returns a project by a given id or null if the project doesn't exist
	 */
	override fun getById(id: Int): Project? {
		return ProjectEntity.select { ProjectEntity.id eq id }.firstOrNull()?.let {
			Project(
				id = it[ProjectEntity.id].value,
				name = it[ProjectEntity.name],
				description = it[ProjectEntity.description]
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
	 * Updates a project entity with the given project
	 */
	override fun update(entity: Project) {
		ProjectEntity.update({ ProjectEntity.id eq entity.id }) {
			it[name] = entity.name
			it[description] = entity.description.orEmpty()
		}
	}
}
