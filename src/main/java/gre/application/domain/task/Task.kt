package gre.application.domain.task

data class Task(
    val taskId: TaskId,
    val taskTitle: String
)

@JvmInline
value class TaskId(val value: Int)
