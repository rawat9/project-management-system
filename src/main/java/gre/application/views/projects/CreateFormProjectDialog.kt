package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.data.binder.BeanValidationBinder
import gre.application.entities.project.Project

class CreateFormProjectDialog(handler: (binder: BeanValidationBinder<Project>) -> Unit) : KComposite() {
	
	private val binder: BeanValidationBinder<Project> = beanValidationBinder<Project>()
	
	private val root = ui {
		dialog {
			headerTitle = "Create new project"
			
			formLayout {
				textField {
					label = "Project name"
					isRequired = true
					bind(binder).bind("projectName")
				}
				
				textArea {
					label = "Description"
					bind(binder).bind("projectDescription")
				}
			}
			
			footer {
				button {
					text = "Cancel"
					onLeftClick { _ -> this@dialog.close() }
				}
				button {
					text = "Create"
					setPrimary()
					
					onLeftClick { _ ->
						handler(binder)
						showSuccess()
					}
				}
			}
			
			isCloseOnOutsideClick = false
		}
	}
	
	fun openDialog() {
		this.root.open()
	}
	
	private fun showSuccess() {
		val notification = Notification.show("Project created successfully")
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS)
		notification.position = Notification.Position.TOP_CENTER
		
		this.root.close()
	}
}

fun HasComponents.createFormProjectDialog(
	handler: (binder: BeanValidationBinder<Project>) -> Unit,
	block: CreateFormProjectDialog.() -> Unit = {},
): CreateFormProjectDialog = init(
	CreateFormProjectDialog(handler),
	block,
)
