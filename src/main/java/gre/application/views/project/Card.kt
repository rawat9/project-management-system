package gre.application.views.project

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

class Card(title: String = "Title", description: String = "Description") : KComposite() {

    private val root = ui {
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

fun HasComponents.card(
    title: String = "Title",
    description: String = "Description",
    block: Card.() -> Unit = {},
): Card = init(
    Card(title, description),
    block,
)
