package gre.application

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
@Theme(value = "pms")
@PWA(name = "Project Management System", shortName = "PMS")
open class Application : AppShellConfigurator

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
