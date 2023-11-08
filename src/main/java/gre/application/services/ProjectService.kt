package gre.application.services

import gre.application.entities.project.Project
import gre.application.repository.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(private val projectRepository: ProjectRepository) {
	
	fun getAll(): List<Project> {
		return projectRepository.getAll()
	}
	
	fun create(entity: Project): Int {
		return projectRepository.create(entity)
	}
	
	fun getById(id: Int): Project? {
		return projectRepository.getById(id)
	}
	
	fun delete(id: Int) {
		return projectRepository.delete(id)
	}
	
	fun save(id: Int, entity: Project) {
		return projectRepository.update(id, entity)
	}
}