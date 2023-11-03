package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.li
import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.karibudsl.v10.text
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems
import com.vaadin.flow.theme.lumo.LumoUtility.Background
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius
import com.vaadin.flow.theme.lumo.LumoUtility.Display
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize
import com.vaadin.flow.theme.lumo.LumoUtility.Margin
import com.vaadin.flow.theme.lumo.LumoUtility.Padding
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor

/**
 * Card component representing a project with an extension function that allows extensibility
 * @param title card title. Default value - "Title"
 * @param description card description. Default value - "Description"
 */
class ProjectCard(title: String = "Title", description: String = "Description") : KComposite() {

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

                addClickListener { _ ->
                    this.ui.ifPresent { ui ->
                        ui.navigate("/tasks/:id")
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
    title: String = "Title",
    description: String = "Description",
    block: ProjectCard.() -> Unit = {},
): ProjectCard = init(
    ProjectCard(title, description),
    block,
)
