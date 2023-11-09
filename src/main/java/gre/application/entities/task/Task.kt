package gre.application.entities.task

import java.time.LocalDate

enum class Status {
	TODO, IN_PROGRESS, DONE
}

enum class Priority {
	LOW, MEDIUM, HIGH
}

data class Task(
	var id: Int,
	var name: String,
	var description: String,
	var projectId: Int,
	var status: Status,
	var priority: Priority,
	var successorId: Int?,
	var deadline: LocalDate
)
