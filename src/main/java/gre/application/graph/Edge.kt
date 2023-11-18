package gre.application.graph

data class Edge<T>(
	val source: Node<T>,
	val destination: Node<T>,
)
