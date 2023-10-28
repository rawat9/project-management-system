package gre.application.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import gre.application.card

@Route("project")
class Project : KComposite() {

    private val root = ui {
        div {
            addClassNames("image-gallery-view")
            addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE)

            horizontalLayout {

                alignItems = FlexComponent.Alignment.CENTER
                justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN

                verticalLayout {
                    h2 {
                        text = "Projects"
                        addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE)
                    }

                    p {
                        text("Projects represents tasks, bugs")
                        addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY)
                    }
                }

                button {
                    text = "Add Projects"
                }
            }
            ol {
                addClassNames(
                    Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE
                )

                card()
                card()
                card()
                card()
                card()
                card()
            }
        }
    }
}