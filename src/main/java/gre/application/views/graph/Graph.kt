package gre.application.views.graph

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.splitLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import de.f0rce.viz.Viz
import de.f0rce.viz.VizFormat

@PageTitle("Graph")
@Route("graph")
class Graph() : KComposite() {
	
	private val graph = "digraph { a; b; c; d; e; f; g }"
	
	init {
		ui {
			splitLayout {
				height = "100vh"
				addToPrimary(getGraph())
				addToSecondary(getMatrix(7))
				setSplitterPosition(60.0)
			}
		}
	}
	
	private fun getGraph(): Div {
		val div = Div()
		val viz = Viz()
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER)
		viz.graph = graph
		viz.width = "100%"
		viz.height = "100%"
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
