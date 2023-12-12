package gre.application.views.graph

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import de.f0rce.viz.Viz
import de.f0rce.viz.VizFormat
import gre.application.graph.Graph
import gre.application.services.TaskService
import org.springframework.beans.factory.annotation.Autowired

@PageTitle("GraphView")
@Route("graph/:projectId")
class GraphView(@Autowired private val taskService: TaskService) : KComposite(), BeforeEnterObserver {
	
	private lateinit var projectId: String
	
	private lateinit var tasksToSuccessorsMap: Map<Int, Collection<Int>>
	
	private lateinit var graphUI: String
	
	private lateinit var graph: Graph<Int>
	
	private lateinit var layout: SplitLayout
	
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
				layout = splitLayout {
					height = "100vh"
					setSplitterPosition(60.0)
				}
			}
		}
	}
	
	override fun beforeEnter(event: BeforeEnterEvent) {
		projectId = event.routeParameters.get("projectId").get()
		tasksToSuccessorsMap = taskService.getAll()
			.filter { task -> task.projectId == projectId.toInt() }
			.sortedBy { it.id }
			.associate { task -> task.id to task.successors }
		
		// initialise an empty graph structure
		graph = Graph(tasksToSuccessorsMap.size)
		
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
		
		graphUI = "digraph { $graph }"
	}
	
	override fun onAttach(attachEvent: AttachEvent?) {
		addGraphToView()
		addTableToView()
	}
	
	private fun addGraphToView() {
		val div = Div()
		val viz = Viz()
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER)
		viz.graph = graphUI
		viz.width = "100%"
		viz.height = "80%"
		viz.format = VizFormat.svg
		div.add(viz)
		layout.addToPrimary(div)
	}

	private fun addTableToView() {
		val m = graph.asAdjacencyMatrix()
		val size = m.size

		val div = Div()
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER)

		val table = Div()
		table.addClassName("table")
		table.style.set("--matrix-size", size.toString())

		val nodes = graph.getNodes()

		val headerRow = Div()
		headerRow.addClassName("tr")

		// empty div to add gap
		headerRow.add(Div())

		nodes.map { node ->
			val headerCell = Div()
			headerCell.addClassName("th")
			headerCell.text = node.data.toString()
			headerRow.add(headerCell)
		}
		table.add(headerRow)

		for (i in 0 until size) {
			// table-row
			val row = Div()
			row.addClassName("tr")

			// table-header
			val th = Div()
			th.addClassName("th")
			th.text = nodes[i].data.toString()
			row.add(th)

			for (j in 0 until size) {
				val cell = Div()
				cell.addClassName("td")
				cell.text = m[i][j].toString()
				row.add(cell)
			}
			table.add(row)
		}
		div.add(table)
		layout.addToSecondary(div)
	}
}
