package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.BadgeVariant
import com.github.mvysny.kaributools.tooltip
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility.*
import gre.application.entities.task.Priority
import gre.application.entities.task.Status
import gre.application.entities.task.Task
import java.time.format.DateTimeFormatter

class TaskCard(val task: Task) : Div() {
	
	private lateinit var succesorCount: Span
	
	init {
		div {
			setId(id.toString())
			addClassName("task-card")
			
			div {
				text(task.name)
			}
			
			onLeftClick {
				fireEvent(LeftClickEvent(this@TaskCard))
			}
			
			div {
				addClassName(Margin.Vertical.SMALL)
				
				icon(VaadinIcon.CIRCLE) {
					setSize("8px")
					color = when (task.status) {
						Status.TODO -> "blue"
						Status.IN_PROGRESS -> "yellow"
						Status.DONE -> "green"
						else -> ""
					}
				}
				span(task.status) {
					addClassNames(
						TextColor.SECONDARY,
						FontSize.SMALL,
						Margin.Left.XSMALL
					)
				}
			}
			val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
			
			horizontalLayout {
				div {
					badge(task.priority) {
						addThemeVariants(BadgeVariant.SMALL, BadgeVariant.PILL)
						when (task.priority) {
							Priority.MEDIUM -> addThemeVariants(BadgeVariant.SUCCESS)
							Priority.HIGH -> addThemeVariants(BadgeVariant.ERROR)
							else -> {}
						}
						addClassNames(Margin.Right.XSMALL)
						tooltip = "Priority"
					}
					badge(formatter.format(task.deadline)) {
						addThemeVariants(
							BadgeVariant.SMALL,
							BadgeVariant.CONTRAST,
							BadgeVariant.PILL
						)
						tooltip = "Deadline"
					}
				}
				
				div {
					addClassNames(Display.FLEX, AlignItems.CENTER, Margin.Left.AUTO, Margin.Right.XSMALL)
					
					icon(VaadinIcon.LINK) {
						setSize("10px")
						color = "gray"
					}
					succesorCount = span {
						addClassNames(Margin.Left.XSMALL, TextColor.SECONDARY, FontSize.SMALL)
					}
				}
			}
		}
	}
	
	fun addLeftClickListener(listener: ComponentEventListener<LeftClickEvent>): Registration {
		return addListener(LeftClickEvent::class.java, listener)
	}
	
	fun setSuccessorCount(count: Int) {
		if (count == 0) {
			succesorCount.parent.get().removeFromParent()
		}
		succesorCount.text = count.toString()
	}
}

/**
 * Events
 */
class LeftClickEvent(source: TaskCard) : ComponentEvent<TaskCard>(source, false)
