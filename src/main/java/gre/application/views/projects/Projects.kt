package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.theme.lumo.LumoUtility.*

@RouteAlias("")
@Route("projects")
class Projects : KComposite() {

    init {
        ui {
            div {
                addClassNames("image-gallery-view")
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
                        h2 {
                            text = "Projects"
                            addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE)
                        }

                        p {
                            text("Projects represents tasks, bugs")
                            addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY)
                        }
                    }

                    val dialog = taskDialog()

                    button {
                        text = "New Project"
                        icon = VaadinIcon.PLUS.create()
                        addClickListener { _ -> dialog.openDialog() }
                    }
                }

                ol {
                    addClassNames(
                        Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE
                    )

                    projectCard(title = "Test project")
                    projectCard(
                        title = "Flashcard",
                        description = "This product is targeting students from college and universities"
                    )
                    projectCard(title = "Kotlin 1815 Coursework")
                }
            }
        }
    }
}