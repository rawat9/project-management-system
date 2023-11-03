package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.textArea
import com.github.mvysny.karibudsl.v10.textField
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

fun HasComponents.taskForm(
    block: CreateProjectForm.() -> Unit = {},
): CreateProjectForm = init(
    CreateProjectForm(),
    block,
)

