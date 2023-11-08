package gre.application.entities.project

data class Project(
	var projectId: Int,
	var projectName: String,
	var projectDescription: String? = null
)
