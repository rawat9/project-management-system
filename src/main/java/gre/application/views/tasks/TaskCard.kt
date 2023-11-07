package gre.application.views.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.BadgeVariant
import com.github.mvysny.kaributools.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.theme.lumo.LumoUtility.*


class TaskCard(title: String) : KComposite() {
	init {
		ui {
			div {
				setId(title)
				addClassName("task-card")
				
				div {
					text(title)
				}
				
				div {
					addClassName(Margin.Vertical.SMALL)
					
					icon(VaadinIcon.CIRCLE) {
						setSize("8px")
						color = "blue"
					}
					span("To Do") {
						addClassNames(
							TextColor.SECONDARY,
							FontSize.SMALL,
							Margin.Left.XSMALL
						)
					}
				}
				
				horizontalLayout {
					div {
						badge("Medium") {
							addThemeVariants(BadgeVariant.SMALL, BadgeVariant.PILL)
							addClassNames(Margin.Right.XSMALL)
							tooltip = "Priority"
						}
						badge("Nov 21, 2023") {
							addThemeVariants(
								BadgeVariant.SMALL,
								BadgeVariant.CONTRAST,
								BadgeVariant.PILL
							)
							tooltip = "Deadline"
						}
					}
					
					badge {
						addClassNames(Margin.Left.AUTO, Background.TRANSPARENT, BorderRadius.LARGE)
						
						icon(VaadinIcon.LINK) {
							setSize("12px")
							color = "gray"
						}
						span("2") {
							addClassNames(Margin.Left.XSMALL, TextColor.SECONDARY)
						}
					}
				}
			}
		}
	}
}

fun HasComponents.taskCard(
	id: String,
	block: TaskCard.() -> Unit = {},
): TaskCard = init(
	TaskCard(id),
	block,
)