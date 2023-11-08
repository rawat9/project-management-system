package gre.application.views.projects

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.theme.lumo.LumoUtility.*
import gre.application.entities.project.Project
import gre.application.services.ProjectService
import org.springframework.beans.factory.annotation.Autowired

@PageTitle("Projects")
@RouteAlias("")
@Route("projects")
class Projects(@Autowired private val projectService: ProjectService) : KComposite() {
	
	init {
		ui {
			div {
				addClassNames("projects-view")
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
					
					val dialog = createFormProjectDialog(fun(binder: BeanValidationBinder<Project>) = onClick(binder))
					
					button {
						text = "New Project"
						icon = VaadinIcon.PLUS.create()
						onLeftClick { _ -> dialog.openDialog() }
					}
				}
				
				ol {
					addClassNames(
						Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE
					)
					
					projectService.getAll().forEach {
						projectCard(title = it.projectName, description = it.projectDescription.orEmpty())
					}
				}
			}
		}
	}
	
	private fun onClick(binder: BeanValidationBinder<Project>) {
		if (binder.validate().isOk) {
			val project = Project(0, "")
			binder.writeBean(project)
			projectService.create(project)
		}
//		UI.getCurrent().page.reload()
	}
}