package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents

class CreateProjectForm : KComposite() {

    init {
        ui {
            formLayout {

                textField {
                    label = "Project name"
                    isRequired = true
                }

                textArea {
                    label = "Description"
                }
            }
        }
    }
}

fun HasComponents.createProjectForm(
    block: CreateProjectForm.() -> Unit = {},
): CreateProjectForm = init(
    CreateProjectForm(),
    block,
)
