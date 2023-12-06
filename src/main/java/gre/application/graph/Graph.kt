package gre.application.graph

class Graph<T>(size: Int) {
	private val adjacencyMap = mutableMapOf<Node<T>, ArrayList<Edge<T>>>()
	private val nodes = arrayListOf<Node<T>>()
	private val matrix = Array(size) { IntArray(size) }
	
	fun createNode(data: T): Node<T> {
		val node = Node(adjacencyMap.count(), data)
		nodes.add(node)
		adjacencyMap[node] = arrayListOf()
		return node
	}
	
	fun addEdge(source: Node<T>, destination: Node<T>) {
		val edge = Edge(source, destination)
		adjacencyMap[source]?.add(edge)
		
		// edge between source and destination
		matrix[source.index][destination.index] = 1
	}
	
	fun asAdjacencyMatrix(): Array<IntArray> {
		return matrix
	}
	
	fun asAdjacencyList(): String {
		return buildString {
			adjacencyMap.forEach { (vertex, edges) ->
				val edgeString = edges.joinToString { it.destination.data.toString() }
				append("${vertex.data} -> [$edgeString]\n")
			}
		}
	}
}