package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import com.vaadin.flow.theme.lumo.LumoUtility.Grid.Column
import gre.application.entities.successor.Successor
import gre.application.entities.task.Priority
import gre.application.entities.task.Status
import gre.application.entities.task.Task
import gre.application.services.ProjectService
import gre.application.services.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.jchristophe.SortableConfig
import org.vaadin.jchristophe.SortableGroupStore
import org.vaadin.jchristophe.SortableLayout
import java.time.LocalDate

@PageTitle("Tasks")
@Route("tasks/:projectId")
class Tasks(@Autowired private val projectService: ProjectService, @Autowired private val taskService: TaskService) :
	KComposite(),
	BeforeEnterObserver {
	
	private lateinit var projectId: String
	
	private lateinit var projectTitle: H2
	
	private lateinit var projectDescription: Paragraph
	
	private lateinit var todoCol: TaskColumn
	
	private lateinit var inProgressCol: TaskColumn
	
	private lateinit var doneCol: TaskColumn
	
	private var tasksForProject: List<Task> = emptyList()
	
	init {
		val sortableConfig = SortableConfig()
		sortableConfig.setGroupName("tasks")
		sortableConfig.allowDragIn(true)
		sortableConfig.allowDragOut(true)
		sortableConfig.animation = 150
		sortableConfig.chosenClass = "task-sortable-chosen"
		sortableConfig.dragClass = "task-sortable-drag"
		sortableConfig.ghostClass = "task-sortable-ghost"
		
		val groupStore = SortableGroupStore()
		
		ui {
			div {
				button {
					addClassNames(Position.FIXED, Margin.SMALL)
					style.set("left", "0")
					text = "Back"
					icon = VaadinIcon.ANGLE_LEFT.create()
					addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST)
					onLeftClick { UI.getCurrent().page.history.back() }
				}
				addClassNames(
					MaxWidth.SCREEN_LARGE,
					Margin.Horizontal.AUTO,
					Padding.Bottom.LARGE,
					Padding.Horizontal.LARGE
				)
				horizontalLayout {
					alignItems = FlexComponent.Alignment.BASELINE
					justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
					
					verticalLayout {
						projectTitle = h2 {
							addClassNames(
								Margin.Bottom.NONE,
								Margin.Top.XLARGE,
								FontSize.XXXLARGE
							)
						}
						
						projectDescription = p {
							addClassNames(
								Margin.Bottom.XLARGE,
								Margin.Top.NONE,
								TextColor.SECONDARY
							)
						}
					}
					
					button {
						text = "New Task"
						icon = VaadinIcon.PLUS.create()
						addThemeVariants(ButtonVariant.LUMO_SMALL)
						onLeftClick {
							val task = Task(
								2,
								"name",
								null,
								projectId.toInt(),
								Status.TODO,
								Priority.LOW,
								LocalDate.now()
							)
							val dialog = TaskFormDialog(Action.CREATE, task)
							dialog.setTasks(tasksForProject)
							dialog.addCreateListener { _ -> taskService.create(task) }
							dialog.openDialog()
						}
					}
					
					button {
						text = "View as graph"
						icon = VaadinIcon.CLUSTER.create()
						addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST)
						onLeftClick {
							UI.getCurrent().navigate("/graph")
						}
					}
					
					button {
						icon = VaadinIcon.ARCHIVE.create()
						text = "Delete project"
						addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR)
					}
				}
				
				div {
					addClassNames(Display.GRID, Column.COLUMNS_3, Gap.MEDIUM, Margin.SMALL)
					
					todoCol = taskColumn("todo")
					inProgressCol = taskColumn("inprogress")
					doneCol = taskColumn("done")
					
					val todoLayout = SortableLayout(todoCol, sortableConfig, groupStore)
					val inProgressLayout = SortableLayout(inProgressCol, sortableConfig, groupStore)
					val doneLayout = SortableLayout(doneCol, sortableConfig, groupStore)
					
					div {
						add(header("To Do"))
						todoLayout.addSortableComponentAddListener { e ->
							val task = (e.component as TaskCard).task
							task.status = Status.TODO
							taskService.update(task)
							UI.getCurrent().page.reload()
						}
						add(todoLayout)
					}
					div {
						add(header("In Progress"))
						inProgressLayout.addSortableComponentAddListener { e ->
							val task = (e.component as TaskCard).task
							task.status = Status.IN_PROGRESS
							taskService.update(task)
							UI.getCurrent().page.reload()
						}
						add(inProgressLayout)
					}
					
					div {
						add(header("Done"))
						doneLayout.addSortableComponentAddListener { e ->
							val task = (e.component as TaskCard).task
							task.status = Status.DONE
							taskService.update(task)
							UI.getCurrent().page.reload()
						}
						add(doneLayout)
					}
				}
			}
		}
	}
	
	override fun beforeEnter(event: BeforeEnterEvent) {
		projectId = event.routeParameters.get("projectId").get()
		val project = projectService.getById(projectId.toInt())
		
		if (project != null) {
			projectTitle.text = project.name
			projectDescription.text = project.description
		}
	}
	
	override fun onAttach(attachEvent: AttachEvent?) {
		tasksForProject = taskService.getAll().filter { it.projectId == projectId.toInt() }
		initColumns()
	}
	
	private fun header(title: String): Div {
		val div = Div()
		div.addClassNames(FontSize.MEDIUM, FontWeight.SEMIBOLD, Margin.Bottom.MEDIUM)
		div.text = title
		return div
	}
	
	/**
	 * Get list of all tasks and add the TaskCard component to the column
	 */
	private fun fetchTasks(column: TaskColumn, status: String): List<Unit> {
		return tasksForProject
			.filter { it.status == status }
			.map {
				val successorCount = it.successors.size
				val taskCard = TaskCard(it)
				taskCard.setSuccessorCount(successorCount)
				attachListener(taskCard)
				column.add(taskCard)
			}
	}
	
	private fun attachListener(taskCard: TaskCard) {
		taskCard.addLeftClickListener {
			val task = taskCard.task
			val dialog = TaskFormDialog(Action.EDIT, task)
			dialog.setTasks(tasksForProject)
			dialog.addSaveListener { taskService.update(task) }
			dialog.addDeleteListener { taskService.delete(task.id) }
			dialog.onSuccessorAddListener { event -> taskService.addSuccessor(event.getData() as Successor) }
			dialog.onSuccessorRemoveListener { event -> taskService.removeSuccessor(event.getData() as Successor) }
			dialog.openDialog()
		}
	}
	
	/**
	 * Initialise all the columns
	 */
	private fun initColumns() {
		fetchTasks(todoCol, Status.TODO)
		fetchTasks(inProgressCol, Status.IN_PROGRESS)
		fetchTasks(doneCol, Status.DONE)
	}
}
