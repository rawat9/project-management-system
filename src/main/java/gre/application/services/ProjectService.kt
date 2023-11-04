package gre.application.services

import gre.application.domain.Dao
import gre.application.domain.project.Project
import gre.application.domain.project.ProjectEntity
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class ProjectService : Dao<Project> {

    override fun getAll(): List<Project> {
        return ProjectEntity.selectAll().map {
            Project(
                projectId = it[ProjectEntity.id].value,
                projectName = it[ProjectEntity.projectName],
            )
        }
    }

    override fun create(entity: Project): Int {
        val id = ProjectEntity.insertAndGetId {
            it[projectName] = entity.projectName
        }
        return id.value
    }

    /**
     * Returns a project by a given id or null if project doesn't exist
     */
    override fun getById(id: Int): Project? {
        return ProjectEntity.select { ProjectEntity.id eq id }.firstOrNull()?.let {
            Project(
                projectId = it[ProjectEntity.id].value,
                projectName = it[ProjectEntity.projectName],
            )
        }
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun save(entity: Project) {
        TODO("Not yet implemented")
    }
}