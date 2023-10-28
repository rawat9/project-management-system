package gre.application.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.Route

@Route("login")
class Login: KComposite() {
    private val root = ui {
        verticalLayout {
            alignItems = FlexComponent.Alignment.CENTER
            width = "auto";

            h1 { text("Login") }
            val username = textField(label = "Username")
            val passwordField = passwordField(label = "Password")
        }
    }
}