package gre.application.entities.task

data class Task(
	val taskId: TaskId,
	val taskTitle: String,
	val taskDescription: String
)

@JvmInline
value class TaskId(val value: Int)
