package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.dialog
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v23.footer
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.HasComponents

class CreateTaskDialog : KComposite() {

    private val root = ui {
        dialog {
            headerTitle = "Create new project"

            taskForm()

            footer {
                button {
                    text = "Cancel"
                    addClickListener { _ -> this@dialog.close() }
                }
                button {
                    text = "Create"
                    setPrimary()
                }
            }

            isCloseOnOutsideClick = false
        }
    }

    fun openDialog() {
        this.root.open()
    }
}

fun HasComponents.taskDialog(
    block: CreateTaskDialog.() -> Unit = {},
): CreateTaskDialog = init(
    CreateTaskDialog(),
    block,
)

