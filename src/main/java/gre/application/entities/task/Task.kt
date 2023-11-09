package gre.application.entities.task

import java.time.LocalDate

data class Task(
	val taskId: Int,
	val taskName: String,
	val taskDescription: String,
	val projectId: Int,
	val status: String,
	val priority: String,
	val successorId: Int?,
	val deadline: LocalDate
)
