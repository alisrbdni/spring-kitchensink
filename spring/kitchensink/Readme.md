kitchensink - Modernization from JBoss to Spring Boot (Java 21)
Author: Your Name / GitHub handle
Date: Month Year
Target: Java 21, Spring Boot, optional MongoDB backend
Source: Originally from Red Hat’s JBoss EAP Quickstarts (the kitchensink example)

1. Background & Purpose
This repository is my modernization of the JBoss EAP kitchensink quickstart application into a Spring Boot (Java 21) application. It was created as part of a Modernization Factory: Developer Candidate Challenge. The goal was to show how a legacy JBoss Java app can be migrated to a more modern platform and approach.

Original Legacy:

The JBoss EAP kitchensink quickstart (GitHub link) used Jakarta EE features (CDI, EJB, JAX-RS, JSF, etc.).
Deployed on a JBoss EAP application server.
New Modern:

Spring Boot (latest stable) + Java 21.
Optional database switch to MongoDB (instead of the old relational/H2 approach).
A containerizable approach with Docker for easy deployment.
A new “modern” UI, plus the original “classic” UI.
During an assessment interview or “playback” session, I’ll:

Demo the running migrated app
Show code changes
Discuss lessons learned
2. Migration Approach
Extract the JBoss kitchensink Code

We took only the relevant kitchensink subfolder, ignoring other quickstarts.
Used an approach simulating “Repomix” tool output and ChatGPT assistance to break down which classes needed rewriting.
Replace Jakarta EE with Spring Boot

CDI -> Spring @Service / @Autowired
JAX-RS -> Spring @RestController
JSF -> Thymeleaf
kitchensink-quickstart-ds.xml or persistence.xml -> Spring Boot config
EJB -> Spring Beans (with optional @Transactional)
Switch from H2 / RDBMS to MongoDB (optional stretch goal)

Replaced JPA Entities with Spring Data Mongo Entities
For demonstration, references a local or remote Mongo instance.
Rewrite the UI

Original “classic” UI retained in members-list.html (Thymeleaf version).
A new “modern” UI in new-ui.html, showing a more “card-based” layout, improved styling.
Ensure Java 21

Adjusted pom.xml to <java.version>21</java.version>
Dockerfile uses eclipse-temurin:21-jdk-alpine
Verify

JUnit tests for integration (similar to the old Arquillian tests, but now with @SpringBootTest).
Manual checks for the new UI.
3. Architecture Overview
Here’s a simplified comparison of old vs. new:

JBoss EAP	Spring Boot
Deploy as WAR/EAR	Single JAR (embedded Tomcat)
JAX-RS @Path("/members")	REST via @RestController("/api/members")
JSF pages (*.xhtml)	Thymeleaf templates (*.html)
EJB, CDI, JPA	@Service, @Autowired, Spring Data (Mongo)
H2/relational DB	Optional: Switch to MongoDB
persistence.xml	application.properties or YAML config
Main modules:

SpringKitchensinkApplication.java: Bootstraps the app (@SpringBootApplication).
MemberController (REST) / MemberPageController + newUiController (UI).
MemberService: Replaces EJB for business logic.
MemberRepository: Replaces JPA DAOs with Mongo (or, if needed, JDBC or JPA).
templates/members-list.html: Original “classic” UI.
templates/new-ui.html: Modern card-based UI.
4. How to Build & Run (Manually)
Check Java version: Must be JDK 21 installed locally.
Clone this repo:
bash
Copy
Edit
git clone https://github.com/<your-username>/spring-kitchensink.git
cd spring-kitchensink
Build:
bash
Copy
Edit
mvn clean package
This produces target/spring-kitchensink-1.0.0-SNAPSHOT.jar.
Run:
bash
Copy
Edit
java -jar target/spring-kitchensink-1.0.0-SNAPSHOT.jar
Visit:
Classic UI: http://localhost:8080/members-ui
New UI: http://localhost:8080/new-ui
REST endpoints: http://localhost:8080/api/members
MongoDB: If you’re using the optional Mongo integration, ensure Mongo is running at mongodb://localhost:27017/ (or adjust application.properties).

5. How to Run Tests
bash
Copy
Edit
mvn test
or

bash
Copy
Edit
mvn verify
This runs JUnit-based integration tests in src/test/java/....

6. How to Run with Docker
We included a Dockerfile referencing Java 21:

dockerfile
Copy
Edit
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/spring-kitchensink-1.0.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
Steps
Build the JAR:
bash
Copy
Edit
mvn clean package
Build the Docker image:
bash
Copy
Edit
docker build -t spring-kitchensink:latest .
Run:
bash
Copy
Edit
docker run -p 8080:8080 spring-kitchensink:latest
Access:
Classic UI: http://localhost:8080/members-ui
New UI: http://localhost:8080/new-ui
7. Extra Info / Observations
We used Thymeleaf + minimal CSS to replicate the original JBoss EAP UI layout and add a new modern layout.
We replaced JAX-RS with standard Spring REST controllers.
If this were a large-scale legacy project, we’d do this migration step by step:
Identify EJB vs. JAX-RS classes, rewrite each to Spring or keep them temporarily in transitional form.
Replace JSF front-ends with Thymeleaf or a modern front-end. Possibly do it piece by piece.
For the data layer, decide if we keep JPA or move to a new DB. Use bridging or parallel data strategies if needed.
For the optional cloud deployment, we can produce a container image, push to a registry, and deploy to e.g. Google Cloud Run.
This entire approach demonstrates how you can break down large migrations: rewrite, test, containerize, push to environment.
8. Next Steps (Interview Playback)
During the interview, I’ll:

Demo the app (both UIs, the REST endpoints).
Show key code changes from EJB/CDI/JSF -> Spring Boot/Thymeleaf.
Highlight any challenges or solutions for data migration to Mongo.
Answer questions about the approach and any best practices learned.
Thank you for reviewing my modernization approach from JBoss EAP’s kitchensink to Spring Boot (Java 21).