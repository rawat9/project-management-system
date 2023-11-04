package gre.application.services

import gre.application.domain.Dao
import gre.application.domain.task.Task
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class TaskService : Dao<Task> {
    override fun getAll(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): Task {
        TODO("Not yet implemented")
    }

    override fun create(entity: Task): Int {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun save(entity: Task) {
        TODO("Not yet implemented")
    }
}