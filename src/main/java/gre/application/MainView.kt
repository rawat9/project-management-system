package gre.application

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.Key
import com.vaadin.flow.router.Route
import gre.application.services.GreetService
import org.springframework.beans.factory.annotation.Autowired

/**
 * A sample Vaadin view class.
 *
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 *
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 *
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 * @param service The message service. Automatically injected Spring managed
 * bean.
 */
class MainView(@Autowired service: GreetService) : KComposite() {
    /**
     * Construct a new Vaadin view.
     *
     * Build the initial UI state for the user accessing the application.
     */
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in
        // styles.css.
        verticalLayout(classNames = "centered-content") {
            // Use TextField for standard text input
            val nameField = textField("Your name") {
                addClassName("bordered")
            }

            // Button click listeners can be defined as lambda expressions
            button("Say hello") {
                // Theme variants give you predefined extra styles for components.
                // Example: Primary button has a more prominent look.
                setPrimary()

                // You can specify keyboard shortcuts for buttons.
                // Example: Pressing enter in this view clicks the Button.
                addClickShortcut(Key.ENTER)

                onLeftClick {
                    this@verticalLayout.h1(service.greet(nameField.value))
                }
            }
        }
    }
}
