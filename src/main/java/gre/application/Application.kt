package gre.application

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import gre.application.graph.AdjacencyList
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

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
	SpringApplication.run(Application::class.java, *args)
	
	val graph = AdjacencyList<String>()
	val node1 = graph.createVertex("A")
	val node2 = graph.createVertex("B")
	val node3 = graph.createVertex("C")
	val node4 = graph.createVertex("D")
	
	graph.addDirectedEdge(node1, node2)
	graph.addDirectedEdge(node1, node3)
	graph.addDirectedEdge(node2, node4)
	graph.addDirectedEdge(node3, node4)
	
	println(graph.toString())
}
