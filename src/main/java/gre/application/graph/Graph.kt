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
		matrix[destination.index][source.index] = 1
	}
	
	/**
	 * Log the graph as an Adjacency Matrix representation
	 */
	fun asAdjacencyMatrix(): Array<IntArray> {
		return matrix
	}
	
	/**
	 * Log the graph as an Adjacency List representation
	 */
	fun asAdjacencyList(): String {
		return buildString {
			adjacencyMap.forEach { (node, edges) ->
				val edgeString = edges.joinToString { it.destination.data.toString() }
				append("${node.data} -> [$edgeString]\n")
			}
		}
	}
	
	override fun toString(): String {
		return buildString {
			adjacencyMap.forEach { (node, edges) ->
				if (edges.isNotEmpty()) {
					val edge =
						edges.joinToString(
							"; ",
							postfix = "; "
						) { "\"Task ${it.source.data.toString()}\"" + " -> " + "\"Task ${it.destination.data.toString()}\"" }
					append(edge)
				} else {
					append("\"Task ${node.data}\"; ")
				}
			}
		}
	}
}