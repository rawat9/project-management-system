package gre.application.entities.project

data class Project(
	var id: Int,
	var name: String,
	var description: String? = null
)
