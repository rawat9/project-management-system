package gre.application.entities.task

import java.time.LocalDate

class Status {
	companion object {
		const val TODO = "To Do"
		const val IN_PROGRESS = "In Progress"
		const val DONE = "Done"
	}
}

class Priority {
	companion object {
		const val LOW = "Low"
		const val MEDIUM = "Medium"
		const val HIGH = "High"
	}
}

data class Task(
	var id: Int,
	var name: String,
	var description: String?,
	var projectId: Int,
	var status: String,
	var priority: String,
	var deadline: LocalDate,
	var successors: Collection<Int> = emptySet()
)
