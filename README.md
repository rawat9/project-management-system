# Project Management System

## Tech Stack

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vaadin Flow](https://vaadin.com/docs/latest/) - uses [Karibu-DSL](https://github.com/mvysny/karibu-dsl)
- Postgres DB
- Kotlin

## Running the Application
There are two ways to run the application :  using `mvn spring-boot:run` or by running the `Application` class directly from your IDE.

#### Intellij IDEA
- On the right side of the window, select Maven --> Plugins --> `spring-boot` --> `spring-boot:run` goal
- Clicking on the green run button will start the application.
- After the application has started, you can view your it at http://localhost:8080/ in your browser.
- If you want to run the application locally in the production mode, use `spring-boot:run -Pproduction` command instead.

## Structure

Vaadin web applications are full-stack and include both client-side and server-side code in the same project.

| Directory | Description |
| :--- | :--- |
| `frontend/` | Client-side source directory |
| &nbsp;&nbsp;&nbsp;&nbsp;`index.html` | HTML template |
| &nbsp;&nbsp;&nbsp;&nbsp;`index.ts` | Frontend entrypoint |
| &nbsp;&nbsp;&nbsp;&nbsp;`main-layout.ts` | Main layout Web Component (optional) |
| &nbsp;&nbsp;&nbsp;&nbsp;`views/` | UI views Web Components (TypeScript / HTML) |
| &nbsp;&nbsp;&nbsp;&nbsp;`styles/` | Styles directory (CSS) |
| `src/main/java/<groupId>/` | Server-side source directory |
| &nbsp;&nbsp;&nbsp;&nbsp;`Application.java` | Server entrypoint |
| &nbsp;&nbsp;&nbsp;&nbsp;`AppShell.java` | application-shell configuration |
