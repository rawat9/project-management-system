package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.data.binder.BeanValidationBinder
import gre.application.entities.project.Project

typealias Handler<T> = (binder: BeanValidationBinder<T>) -> Unit

class ProjectFormDialog(handler: Handler<Project>) : KComposite() {
	
	private val binder: BeanValidationBinder<Project> = beanValidationBinder<Project>()
	
	private val root = ui {
		dialog {
			headerTitle = "Create new project"
			
			formLayout {
				textField {
					label = "Project name"
					isRequired = true
					binder.forField(this).asRequired("Field is required").bind("name")
				}
				
				textArea {
					label = "Description"
					binder.forField(this).bind("description")
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
					isEnabled = false
					
					binder.addStatusChangeListener { event ->
						val isValid = event.binder.isValid
						this.isEnabled = isValid
					}
					
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

fun HasComponents.projectFormDialog(
	handler: (binder: BeanValidationBinder<Project>) -> Unit,
	block: ProjectFormDialog.() -> Unit = {},
): ProjectFormDialog = init(
	ProjectFormDialog(handler),
	block,
)
