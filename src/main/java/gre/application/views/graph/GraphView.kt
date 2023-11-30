package gre.application.views.graph

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.*
import com.vaadin.flow.theme.lumo.LumoUtility.*
import de.f0rce.viz.Viz
import de.f0rce.viz.VizFormat
import gre.application.graph.Graph
import gre.application.services.TaskService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired

@PageTitle("GraphView")
@Route("graph/:projectId")
class GraphView(@Autowired private val taskService: TaskService) : KComposite(), BeforeEnterObserver,
	HasErrorParameter<NotFoundException> {
	
	private lateinit var projectId: String
	
	private lateinit var tasksToSuccessorsMap: Map<Int, Collection<Int>>;
	
	private val graph = "digraph { a -> b; a -> c; b -> d; c -> d }"
	
	init {
		ui {
			div {
				button {
					addClassNames(Position.FIXED, Margin.SMALL)
					style.set("left", "0").set("z-index", "10")
					text = "Back to board view"
					icon = VaadinIcon.ANGLE_LEFT.create()
					addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST)
					onLeftClick { UI.getCurrent().page.history.back() }
				}
				splitLayout {
					height = "100vh"
					addToPrimary(getGraph())
					addToSecondary(getMatrix(5))
					setSplitterPosition(60.0)
				}
			}
		}
	}
	
	override fun beforeEnter(event: BeforeEnterEvent) {
		projectId = event.routeParameters.get("projectId").get()
		tasksToSuccessorsMap = taskService.getAll()
			.filter { task -> task.projectId == projectId.toInt() }.associate { task -> task.id to task.successors }
		
		// initialise an empty graph structure
		val graph = Graph<Int>()
		
		// generate nodes
		val nodes = tasksToSuccessorsMap.mapKeys { graph.createNode(it.key) }.keys
		
		// add edges between nodes
		nodes.forEach { node ->
			val successors = tasksToSuccessorsMap[node.data].orEmpty()
			if (successors.isNotEmpty()) {
				successors.forEach { successor ->
					val destinationNode = nodes.find { it.data == successor }
					if (destinationNode != null) {
						graph.addEdge(node, destinationNode)
					}
				}
			}
		}
		
		println(graph.asAdjacencyList())
	}
	
	private fun getGraph(): Div {
		val div = Div()
		val viz = Viz()
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER)
		viz.graph = graph
		viz.width = "100%"
		viz.height = "80%"
		viz.format = VizFormat.svg
		div.add(viz)
		return div
	}
	
	private fun getMatrix(size: Int): Div {
		val div = Div()
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER)
		
		val matrix = Div()
		matrix.addClassName("matrix")
		matrix.style.set("--matrix-size", size.toString())
		
		for (i in 0 until size) {
			val row = Div()
			row.addClassName("row")
			for (j in 0 until size) {
				val cell = Div()
				cell.addClassName("cell")
				cell.text = "0"
				row.add(cell)
			}
			matrix.add(row)
		}
		div.add(matrix)
		return div
	}
	
	override fun setErrorParameter(event: BeforeEnterEvent, p1: ErrorParameter<NotFoundException>): Int {
		element.text = "Route not found"
		return HttpServletResponse.SC_NOT_FOUND
	}
}
