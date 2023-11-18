package gre.application.graph

class AdjacencyList<T> {
	private val adjacencyMap = mutableMapOf<Node<T>, ArrayList<Edge<T>>>()
	
	fun createVertex(data: T): Node<T> {
		val vertex = Node(adjacencyMap.count(), data)
		adjacencyMap[vertex] = arrayListOf()
		return vertex
	}
	
	fun addDirectedEdge(source: Node<T>, destination: Node<T>) {
		val edge = Edge(source, destination)
		adjacencyMap[source]?.add(edge)
	}
	
	override fun toString(): String {
		return buildString {
			adjacencyMap.forEach { (vertex, edges) ->
				val edgeString = edges.joinToString { it.destination.data.toString() }
				append("${vertex.data} -> [$edgeString]\n")
			}
		}
	}
}

class AdjacencyMatrix<T> {
	private val vertices = arrayListOf<Node<T>>()
	private val matrix = Array(5) { IntArray(5) }
	
	fun createVertex(data: T): Node<T> {
		val node = Node(vertices.count(), data)
		vertices.add(node)
		return node
	}
	
	fun addDirectedEdge(source: Node<T>, destination: Node<T>) {
		matrix[source.index][destination.index] = 1
	}
	
	fun print() {
		for (node in vertices) {
			print(node.data)
		}
		println()
		for (i in matrix.indices) {
			for (j in 0 until matrix[i].size) {
				print("${matrix[i][j]} ")
			}
			println()
		}
	}
}
