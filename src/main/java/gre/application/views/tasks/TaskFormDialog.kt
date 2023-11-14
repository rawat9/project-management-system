package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent
import com.vaadin.flow.theme.lumo.LumoUtility.Margin
import gre.application.entities.task.Priority
import gre.application.entities.task.Status
import gre.application.entities.task.Task

/**
 * Form action:
 * - EDIT - Edit/Update an existing task
 * - CREATE - creating a new task
 */
enum class Action {
	EDIT, CREATE
}

class TaskFormDialog(val action: Action, val task: Task) : KComposite() {
	
	private val binder = beanValidationBinder<Task>()
	
	private lateinit var successors: Select<Int?>
	
	private val root = ui {
		dialog {
			headerTitle = if (action.name == "CREATE") "Create new task" else "Edit task"
			
			if (action.name == "EDIT") {
				binder.bean = task
			}
			
			formLayout {
				width = "520px"
				
				setResponsiveSteps(
					FormLayout.ResponsiveStep("500px", 3)
				)
				
				textField {
					label = "Task name"
					isRequired = true
					colspan = 3
					bind(binder).bind("name")
				}
				
				textArea {
					label = "Description"
					colspan = 3
					bind(binder).bind("description")
				}
				
				select<String> {
					label = "Status"
					setItems(Status.TODO, Status.IN_PROGRESS, Status.DONE)
					bind(binder).bind("status")
				}
				
				select<String> {
					label = "Priority"
					setItems(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
					bind(binder).bind("priority")
				}
				
				datePicker {
					label = "Deadline"
					bind(binder).bind("deadline")
				}
				
				successors = select<Int?> {
					label = "Successor"
					colspan = 3
				}
				
				horizontalLayout {
					colspan = 3
					
					// manually remove the style so can be overridden
					style.remove("justify-content")
					addClassNames(Margin.Top.MEDIUM, JustifyContent.END)
					
					button {
						text = "Cancel"
						onLeftClick { closeDialog() }
					}
					button {
						text = if (action.name == "EDIT") "Save" else "Create"
						setPrimary()
						
						onLeftClick {
							if (action.name == "CREATE") {
								binder.writeBean(task)
								fireEvent(CreateEvent(this@formLayout, task))
							} else {
								if (binder.validate().isOk) {
									fireEvent(SaveEvent(this@formLayout, binder.bean))
								}
							}
							showSuccess()
						}
					}
				}
				
				isCloseOnOutsideClick = false
			}
		}
	}
	
	fun openDialog() {
		this.root.open()
	}
	
	private fun closeDialog() {
		this.root.close()
	}
	
	fun setTasks(tasksForProject: List<Task>) {
		val items = successors.setItems(tasksForProject.map { it.id })
		if (action.name == "EDIT") {
			items.removeItem(task.id)
		}
		binder.forField(successors).bind("successorId")
		successors.value = task.successorId
		successors.setItemLabelGenerator { id ->
			val foundTask = tasksForProject.find { it.id == id }
			if (foundTask !== null) "$id - ${foundTask.name}" else ""
		}
	}
	
	private fun showSuccess() {
		val notification =
			if (action.name == "CREATE") Notification.show("Task created successfully") else Notification.show("Task updated successfully")
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS)
		notification.position = Notification.Position.TOP_CENTER
		this.closeDialog()
	}
	
	fun addSaveListener(listener: ComponentEventListener<SaveEvent>): Registration {
		return addListener(SaveEvent::class.java, listener)
	}
	
	fun addCreateListener(listener: ComponentEventListener<CreateEvent>): Registration {
		return addListener(CreateEvent::class.java, listener)
	}
}

/**
 * Events
 */
abstract class TaskFormEvent protected constructor(source: KFormLayout, task: Task) :
	ComponentEvent<KFormLayout>(source, false) {
	private val task: Task
	
	init {
		this.task = task
	}
	
	fun getTask(): Task {
		return task
	}
}

class CreateEvent(source: KFormLayout, task: Task) : TaskFormEvent(source, task)

class SaveEvent(source: KFormLayout, task: Task) : TaskFormEvent(source, task)
