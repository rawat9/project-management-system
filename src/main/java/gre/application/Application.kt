package gre.application

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * The entry point of the Spring Boot application.
 * - Configure the database and connection pool
 * - Initialise the schema and create DB tables
 * - Set up the application UI theme
 */
@SpringBootApplication
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
@Theme(value = "pms")
@PWA(name = "Project Management System", shortName = "PMS")
open class Application : AppShellConfigurator

fun main(args: Array<String>) {
	SpringApplication.run(Application::class.java, *args)
}
