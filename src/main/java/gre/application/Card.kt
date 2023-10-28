package gre.application

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.theme.lumo.LumoUtility.*

class Card() : KComposite() {

    private val root = ui {
        li {
            addClassNames(
                Background.CONTRAST_5,
                Display.FLEX,
                FlexDirection.COLUMN,
                AlignItems.START,
                Padding.MEDIUM,
                BorderRadius.LARGE
            )

            span {
                addClassNames(
                    FontSize.LARGE,
                )
                text = "Title"
            }

            span {
                addClassNames(
                    FontSize.SMALL,
                    TextColor.SECONDARY
                )
                text("Card subtitle")
            }

            p {
                text("this is the description")
                addClassName(
                    Margin.Vertical.MEDIUM
                )
            }

            span {
                element.setAttribute("theme", "badge")
                text("Label")
            }
        }
    }
}

fun HasComponents.card(block: Card.()->Unit = {}): Card = init(Card(), block)