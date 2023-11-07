package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.Badge
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import com.vaadin.flow.theme.lumo.LumoUtility.Grid.Column
import org.vaadin.jchristophe.SortableConfig
import org.vaadin.jchristophe.SortableGroupStore
import org.vaadin.jchristophe.SortableLayout

@PageTitle("Tasks")
@Route("tasks/:projectName")
class Tasks : KComposite(), BeforeEnterObserver {
	
	private var projectName = ""
	
	private lateinit var projectTitle: H2;
	
	init {
		val sortableConfig = SortableConfig()
		sortableConfig.setGroupName("tasks");
		sortableConfig.allowDragIn(true);
		sortableConfig.allowDragOut(true);
		sortableConfig.animation = 150;
		sortableConfig.chosenClass = "task-sortable-chosen";
		sortableConfig.dragClass = "task-sortable-drag";
		sortableConfig.ghostClass = "task-sortable-ghost";
		
		val groupStore = SortableGroupStore()
		
		ui {
			div {
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
						
						p {
							text("Projects description")
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
					}
				}
				
				div {
					addClassNames(Display.GRID, Column.COLUMNS_3, Gap.MEDIUM, Margin.SMALL)
					
					val todoCol = taskColumn("todo") {
						addTasks(this, 6)
					}
					val inProgressCol = taskColumn("inprogress") {
						addTasks(this, 1)
					}
					val doneCol = taskColumn("done") {
						taskCard("4")
					}
					
					val todoLayout = SortableLayout(todoCol, sortableConfig, groupStore)
					val inProgressLayout = SortableLayout(inProgressCol, sortableConfig, groupStore)
					val doneLayout = SortableLayout(doneCol, sortableConfig, groupStore)
					
					div {
						add(header("To Do"))
						add(todoLayout)
					}
					div {
						add(header("In Progress"))
						inProgressLayout.addSortableComponentAddListener { e ->
							if (e.component.id.get().startsWith("todo")) {
								Notification.show("task is in-progress")
							} else {
								Notification.show("back to in-progress")
							}
						}
						add(inProgressLayout)
					}
					
					div {
						add(header("Done"))
						doneLayout.addSortableComponentAddListener { e ->
							if (e.component.id.get().startsWith("inprogress")) {
								Notification.show("task has been completed")
							} else {
								Notification.show("...")
							}
						}
						add(doneLayout)
					}
				}
			}
		}
	}
	
	override fun beforeEnter(event: BeforeEnterEvent) {
		projectName = event.routeParameters.get("projectName").get()
		projectTitle.text = projectName
	}
	
	private fun header(title: String): Div {
		val div = Div()
		div.addClassNames(FontSize.MEDIUM, FontWeight.SEMIBOLD, Margin.Bottom.MEDIUM)
		div.text = title
		val badge = Badge("5")
		badge.addClassName(Margin.Left.SMALL)
		div.add(badge)
		
		return div
	}
	
	private fun addTasks(column: TaskColumn, taskNumber: Int) {
		for (i in 0 until taskNumber) {
			val id = "${column.id.get()}-${i}"
			column.add(TaskCard(id))
		}
	}
}
