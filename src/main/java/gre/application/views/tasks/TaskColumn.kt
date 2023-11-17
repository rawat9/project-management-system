package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.html.Div

class TaskColumn(val title: String) : Div() {
	init {
		setId(title)
		addClassNames("column")
	}
}

fun HasComponents.taskColumn(
	id: String,
	block: TaskColumn.() -> Unit = {},
): TaskColumn = init(
	TaskColumn(id),
	block,
)
