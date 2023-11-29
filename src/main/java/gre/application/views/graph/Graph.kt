package gre.application.views.graph

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import de.f0rce.viz.Viz
import de.f0rce.viz.VizFormat
import gre.application.services.TaskService
import org.springframework.beans.factory.annotation.Autowired

@PageTitle("Graph")
@Route("graph")
class Graph(@Autowired private val taskService: TaskService) : KComposite() {
	
	private val graph = "digraph { a; b; c; d; e; f; g }"
	
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
					addToSecondary(getMatrix(7))
					setSplitterPosition(60.0)
				}
			}
		}
	}
	
	override fun onAttach(attachEvent: AttachEvent?) {
		val successors = taskService.getAll().map { task -> listOf(task.id, task.successors) }
		println(successors)
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
}
