package gre.application.services

import gre.application.entities.task.Task
import gre.application.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {
	fun getAll(): List<Task> {
		return taskRepository.getAll()
	}
	
	fun create(entity: Task): Int {
		return taskRepository.create(entity)
	}
	
	fun getById(id: Int): Task? {
		return taskRepository.getById(id)
	}
	
	fun delete(id: Int) {
		return taskRepository.delete(id)
	}
	
	fun update(id: Int, entity: Task) {
		return taskRepository.update(id, entity)
	}
}