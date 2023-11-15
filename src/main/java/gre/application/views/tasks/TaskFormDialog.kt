package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.binder.ValidationException
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
					colspan = 3
					binder.forField(this).asRequired("Field is required").bind("name")
				}
				
				textArea {
					label = "Description"
					colspan = 3
					binder.forField(this).bind("description")
				}
				
				select<String> {
					label = "Status"
					setItems(Status.TODO, Status.IN_PROGRESS, Status.DONE)
					binder.forField(this).asRequired("Field is required").bind("status")
				}
				
				select<String> {
					label = "Priority"
					setItems(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
					binder.forField(this).asRequired("Field is required").bind("priority")
				}
				
				datePicker {
					label = "Deadline"
					binder.forField(this).asRequired("Field is required").bind("deadline")
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
						isEnabled = false
						
						binder.addStatusChangeListener { event ->
							val isValid = event.binder.isValid
							this.isEnabled = isValid
						}
						
						onLeftClick {
							try {
								if (action.name == "CREATE") {
									binder.writeBeanIfValid(task)
									fireEvent(CreateEvent(this@formLayout, task))
								} else {
									if (binder.isValid) {
										fireEvent(SaveEvent(this@formLayout, binder.bean))
									}
								}
								showSuccess()
							} catch (e: ValidationException) {
								notifyValidationError()
							}
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
		if (action.name == "CREATE") {
			showSubmitSuccess("Task created successfully").open()
		} else {
			showSubmitSuccess("Task updated successfully").open()
		}
		this.closeDialog()
	}
	
	private fun showSubmitSuccess(text: String): Notification {
		val notification = Notification()
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS)
		val icon = VaadinIcon.CHECK_CIRCLE.create()
		
		val closeBtn = Button(VaadinIcon.CLOSE_SMALL.create()) { _ -> notification.close() }
		closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)
		
		val layout = HorizontalLayout(icon, Text(text), closeBtn)
		layout.alignItems = FlexComponent.Alignment.CENTER
		notification.duration = 2000
		notification.position = Notification.Position.TOP_CENTER
		notification.add(layout)
		return notification
	}
	
	private fun notifyValidationError() {
		Notification.show("Something went wrong", 2000, Notification.Position.TOP_CENTER)
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
