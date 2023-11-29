package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v23.multiSelectComboBox
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.MultiSelectComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility.Margin
import gre.application.entities.successor.Successor
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

class TaskFormDialog(private val action: Action, val task: Task) : KComposite() {
	
	private val binder = beanValidationBinder<Task>()
	
	private lateinit var successors: MultiSelectComboBox<Int>
	
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
				
				successors = multiSelectComboBox {
					isVisible = action.name == "EDIT"
					label = "Successors"
					colspan = 3
				}
				
				successors.addSelectionListener { event ->
					if (event.addedSelection.isNotEmpty()) {
						val successorId = event.addedSelection.elementAt(0)
						fireEvent(AddSuccessorEvent(event.source, Successor(id = successorId, taskId = task.id)))
					}
					
					if (event.removedSelection.isNotEmpty()) {
						val successorId = event.removedSelection.elementAt(0)
						fireEvent(RemoveSuccessorEvent(event.source, Successor(id = successorId, taskId = task.id)))
					}
				}
				
				horizontalLayout {
					colspan = 3
					
					// manually remove the style so can be overridden
					style.remove("justify-content")
					addClassNames(Margin.Top.MEDIUM)
					
					button {
						text = "Delete"
						addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR)
						onLeftClick {
							fireEvent(DeleteEvent(this@formLayout))
							UI.getCurrent().page.reload()
						}
					}
					
					div {
						addClassName(Margin.Left.AUTO)
						button {
							text = "Cancel"
							addClassName(Margin.Right.SMALL)
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
										fireEvent(CreateEvent(this@formLayout))
									} else {
										if (binder.isValid) {
											fireEvent(SaveEvent(this@formLayout))
										}
									}
									UI.getCurrent().page.reload()
									showSuccess()
								} catch (e: ValidationException) {
									notifyValidationError()
								}
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
		binder.forField(successors).bind("successors")
		successors.value = task.successors.toSet()
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
	
	fun onSuccessorAddListener(listener: ComponentEventListener<AddSuccessorEvent>): Registration {
		return addListener(AddSuccessorEvent::class.java, listener)
	}
	
	fun onSuccessorRemoveListener(listener: ComponentEventListener<RemoveSuccessorEvent>): Registration {
		return addListener(RemoveSuccessorEvent::class.java, listener)
	}
	
	fun addCreateListener(listener: ComponentEventListener<CreateEvent>): Registration {
		return addListener(CreateEvent::class.java, listener)
	}
	
	fun addDeleteListener(listener: ComponentEventListener<DeleteEvent>): Registration {
		return addListener(DeleteEvent::class.java, listener)
	}
}

/**
 * Events
 */
abstract class Event<Source : Component> protected constructor(source: Source, data: Any? = null) :
	ComponentEvent<Source>(source, false) {
	private val data: Any?
	
	init {
		this.data = data
	}
	
	fun getData(): Any? {
		return data
	}
}

class CreateEvent(source: KFormLayout) : Event<KFormLayout>(source)

class DeleteEvent(source: KFormLayout) : Event<KFormLayout>(source)

class SaveEvent(source: KFormLayout) : Event<KFormLayout>(source)

class AddSuccessorEvent(source: MultiSelectComboBox<Int>, data: Successor) :
	Event<MultiSelectComboBox<Int>>(source, data)

class RemoveSuccessorEvent(source: MultiSelectComboBox<Int>, data: Successor) :
	Event<MultiSelectComboBox<Int>>(source, data)
