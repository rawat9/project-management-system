package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.theme.lumo.LumoUtility.*

/**
 * Card component representing a project with an extension function that allows extensibility
 * @param id card id.
 * @param title card title. Default value - "Title"
 * @param description card description. Default value - "Description"
 */
class ProjectCard(id: Int, title: String = "Title", description: String = "Description") : KComposite() {
	
	init {
		ui {
			li {
				addClassNames(
					Background.CONTRAST_5,
					Display.FLEX,
					FlexDirection.COLUMN,
					AlignItems.START,
					Padding.MEDIUM,
					BorderRadius.LARGE,
				)
				
				onLeftClick { _ ->
					this.ui.ifPresent { ui ->
						ui.navigate("/tasks/${id}")
					}
				}
				
				span {
					addClassNames(
						FontSize.LARGE,
					)
					text(title)
				}
				
				span {
					addClassNames(
						FontSize.SMALL,
						Margin.Vertical.MEDIUM,
						TextColor.SECONDARY,
					)
					text(description)
				}
			}
		}
	}
}

fun HasComponents.projectCard(
	id: Int,
	title: String = "Title",
	description: String = "Description",
	block: ProjectCard.() -> Unit = {},
): ProjectCard = init(
	ProjectCard(id, title, description),
	block,
)
