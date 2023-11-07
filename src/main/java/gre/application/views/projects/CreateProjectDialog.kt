package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.HasComponents

class CreateProjectDialog : KComposite() {
	
	private val root = ui {
		dialog {
			headerTitle = "Create new project"
			
			createProjectForm()
			
			footer {
				button {
					text = "Cancel"
					onLeftClick { _ -> this@dialog.close() }
				}
				button {
					text = "Create"
					setPrimary()
				}
			}
			
			isCloseOnOutsideClick = false
		}
	}
	
	fun openDialog() {
		this.root.open()
	}
}

fun HasComponents.createProjectDialog(
	block: CreateProjectDialog.() -> Unit = {},
): CreateProjectDialog = init(
	CreateProjectDialog(),
	block,
)

