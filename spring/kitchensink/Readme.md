```markdown
# Kitchensink - Modernization from JBoss to Spring Boot (Java 21)

**Target:** Java 21, Spring Boot, with MongoDB backend
**Source:** Originally from Red Hat’s JBoss EAP Quickstarts (the kitchensink example) To Spring Boot (Java 21) migration with MongoDB as new database.  The application now utilizes **MongoDB Atlas** for the database, pre-populated with data via a seeder script.

**This application is deployed with Docker on GCP Cloud Run and is accessible at: [https://kitchensink-93677313045.us-central1.run.app/members-ui](https://kitchensink-93677313045.us-central1.run.app/members-ui). It includes the classic UI and a new modern UI option.**

## 1. Background & Purpose

This repository showcases the modernization of the JBoss EAP kitchensink quickstart application into a Spring Boot (Java 21) application. It was created as part of a Modernization Factory: Developer Candidate Challenge. The goal was to demonstrate how a legacy JBoss Java application can be migrated to a more modern platform and approach.

**Original Legacy:**

*   The JBoss EAP kitchensink quickstart ([GitHub link](https://github.com/jbossas/jboss-eap-quickstarts)) used Jakarta EE features (CDI, EJB, JAX-RS, JSF, etc.).
*   Deployed on a JBoss EAP application server.

**New Modern:**

*   Spring Boot (latest stable) + Java 21.
*   Database switch to MongoDB Atlas (instead of the old relational/H2 approach), with a seeder script to populate initial data.
*   A containerizable approach with Docker for easy deployment, including to platforms like GCP Cloud Run.
*   A new “modern” UI, plus the original “classic” UI.

During an assessment interview or “playback” session, I will:

*   Demo the running migrated app
*   Show code changes
*   Discuss lessons learned

## 2. Migration Approach

*   **Extract the JBoss kitchensink Code:** We took only the relevant kitchensink subfolder, ignoring other quickstarts. Used an approach simulating “Repomix” tool output and ChatGPT assistance to break down which classes needed rewriting.
*   **Replace Jakarta EE with Spring Boot:**

    *   CDI -> Spring `@Service` / `@Autowired`
    *   JAX-RS -> Spring `@RestController`
    *   JSF -> Thymeleaf
    *   `kitchensink-quickstart-ds.xml` or `persistence.xml` -> Spring Boot config (`application.properties` or `application.yml`)
    *   EJB -> Spring Beans (with optional `@Transactional`)
*   **Switch from H2 / RDBMS to MongoDB Atlas:**

    *   Replaced JPA Entities with Spring Data Mongo Entities
    *   Configuration now uses connection details for MongoDB Atlas.
    *   A seeder script (e.g., using Spring Data's `MongoTemplate` or a similar approach) ensures initial data is present in the database.
*   **Rewrite the UI:**

    *   Original “classic” UI retained in `members-list.html` (Thymeleaf version).
    *   A new “modern” UI in `new-ui.html`, showing a more “card-based” layout, improved styling.
*   **Ensure Java 21:**

    *   Adjusted `pom.xml` to `<java.version>21</java.version>`
    *   Dockerfile uses `eclipse-temurin:21-jdk-alpine`
*   **Verify:**

    *   JUnit tests for integration (similar to the old Arquillian tests, but now with `@SpringBootTest`).
    *   Manual checks for the new UI.
    *   Verify data is loaded correctly from MongoDB Atlas after deployment.

## 3. Architecture Overview

Here’s a simplified comparison of old vs. new:

| JBoss EAP                     | Spring Boot                               |
| ----------------------------- | ----------------------------------------- |
| Deploy as WAR/EAR             | Single JAR (embedded Tomcat)              |
| JAX-RS `@Path("/members")`    | REST via `@RestController("/api/members")` |
| JSF pages (`*.xhtml`)         | Thymeleaf templates (`*.html`)            |
| EJB, CDI, JPA                | `@Service`, `@Autowired`, Spring Data (Mongo)         |
| H2/relational DB              | MongoDB Atlas                             |
| `persistence.xml`             | `application.properties` or YAML config        |

**Main modules:**

*   `SpringKitchensinkApplication.java`: Bootstraps the app (`@SpringBootApplication`).
*   `MemberController` (REST) / `MemberPageController` + `newUiController` (UI).
*   `MemberService`: Replaces EJB for business logic.
*   `MemberRepository`: Replaces JPA DAOs with Mongo (or, if needed, JDBC or JPA).
*   `templates/members-list.html`: Original “classic” UI.
*   `templates/new-ui.html`: Modern card-based UI.
*   **Seeder Script:**  A component or class responsible for loading initial data into MongoDB Atlas during application startup.

## 4. How to Build & Run (Manually)

*   **Check Java version:** Must be JDK 21 installed locally.
*   **Clone this repo:**

    ```bash
    git clone https://github.com/<your-username>/spring-kitchensink.git
    cd spring-kitchensink
    ```
*   **Build:**

    ```bash
    mvn clean package
    ```

    This produces `target/spring-kitchensink-1.0.0-SNAPSHOT.jar`.
*   **Run:**

    ```bash
    java -jar target/spring-kitchensink-1.0.0-SNAPSHOT.jar
    ```
*   **Visit:**

    *   Classic UI: [http://localhost:8080/members-ui](http://localhost:8080/members-ui)
    *   New UI: [http://localhost:8080/new-ui](http://localhost:8080/new-ui)
    *   REST endpoints: [http://localhost:8080/api/members](http://localhost:8080/api/members)

*   **MongoDB Atlas:**

    *   Ensure you have a MongoDB Atlas cluster set up.
    *   Update the `application.properties` or `application.yml` file with the correct connection string for your MongoDB Atlas cluster.  This typically includes the username, password, and the cluster URL.  **Be careful not to commit sensitive information directly into your repository.** Use environment variables instead.
    * Ensure you configure `IP Whitelisting` on MongoDB atlas.

## 5. How to Run Tests

```bash
mvn test
```

or

```bash
mvn verify
```

This runs JUnit-based integration tests in `src/test/java/...`.

## 6. How to Run with Docker

We included a `Dockerfile` referencing Java 21:

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/spring-kitchensink-1.0.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

**Steps:**

*   **Build the JAR:**

    ```bash
    mvn clean package
    ```
*   **Build the Docker image:**

    ```bash
    docker build -t spring-kitchensink:latest .
    ```
*   **Run (locally):**

    ```bash
    docker run -p 8080:8080 spring-kitchensink:latest
    ```

    Access:

    *   Classic UI: [http://localhost:8080/members-ui](http://localhost:8080/members-ui)
    *   New UI: [http://localhost:8080/new-ui](http://localhost:8080/new-ui)

*   **Run on GCP Cloud Run (Example):**

    These steps assume you have a Google Cloud project set up and the `gcloud` CLI installed and configured.  **Crucially, you'll need to configure environment variables in Cloud Run for your MongoDB Atlas connection string.**

    1.  **Push the image to Google Container Registry (GCR) / Artifact Registry:**

        ```bash
        gcloud auth configure-docker  # Authenticate Docker with gcloud
        docker tag spring-kitchensink:latest us-central1-docker.pkg.dev/<your-gcp-project-id>/spring-kitchensink-repo/spring-kitchensink:latest
        docker push us-central1-docker.pkg.dev/<your-gcp-project-id>/spring-kitchensink-repo/spring-kitchensink:latest
        ```

        Replace `<your-gcp-project-id>` with your actual Google Cloud project ID. You might need to create a repository in Artifact Registry first. Choose a region appropriate for your project. I'm using `us-central1` in this example.
    2.  **Deploy to Cloud Run:**

        ```bash
        gcloud run deploy spring-kitchensink \
            --image us-central1-docker.pkg.dev/<your-gcp-project-id>/spring-kitchensink-repo/spring-kitchensink:latest \
            --platform managed \
            --region us-central1 \
            --allow-unauthenticated \
            --set-env-vars SPRING_DATA_MONGODB_URI="<your-mongodb-atlas-connection-string>"
        ```

        Replace `<your-mongodb-atlas-connection-string>` with the full connection string for your MongoDB Atlas cluster.  **This is critically important!** Ensure the connection string includes the username, password, and database name. This command deploys the `spring-kitchensink` service to Cloud Run, using the image you pushed. It also allows unauthenticated access for demo purposes. Adjust the region (`--region`) and authentication settings as needed. Cloud Run will output the service URL after deployment.

    3. **Set Environment variables via gcloud (alternative method):** If you missed setting the environment variable during deployment, or need to update it:

        ```bash
        gcloud run services update spring-kitchensink --set-env-vars SPRING_DATA_MONGODB_URI="<your-mongodb-atlas-connection-string>"
        ```
        This command add the environment variables for the MongoDB Atlas connection string which are necessary to run the app without errors. **Important: using this command restarts the service.**

## 7. Extra Info / Observations

*   We used Thymeleaf + minimal CSS to replicate the original JBoss EAP UI layout and add a new modern layout.
*   We replaced JAX-RS with standard Spring REST controllers.
*   The data is now persisted in MongoDB Atlas, a cloud-based NoSQL database.
*   If this were a large-scale legacy project, we'd do this migration step by step:

    *   Identify EJB vs. JAX-RS classes, rewrite each to Spring or keep them temporarily in transitional form.
    *   Replace JSF front-ends with Thymeleaf or a modern front-end. Possibly do it piece by piece.
    *   For the data layer, decide if we keep JPA or move to a new DB. Use bridging or parallel data strategies if needed.
*   For the optional cloud deployment, we can produce a container image, push to a registry, and deploy to e.g. Google Cloud Run.
*   This entire approach demonstrates how you can break down large migrations: rewrite, test, containerize, push to environment.

## 8. Next Steps (Interview Playback)

During the interview, I will:

*   Demo the app (both UIs, the REST endpoints, including the deployed instance on Cloud Run if applicable).
*   Show key code changes from EJB/CDI/JSF -> Spring Boot/Thymeleaf.
*   Highlight any challenges or solutions for data migration to Mongo, including configuring the MongoDB Atlas connection and the implementation of the data seeder.
*   Answer questions about the approach and any best practices learned.

Thank you for reviewing my modernization approach from JBoss EAP’s kitchensink to Spring Boot (Java 21).
```
Key changes and explanations:

*   **MongoDB Atlas and Seeder Script:** The description now explicitly states that the application uses MongoDB Atlas and includes a seeder script.
*   **Seeder Script Explanation:** Added a brief explanation of what the seeder script is and its purpose.
*   **Updated MongoDB Atlas Configuration:** The instructions for running the application (both manually and in Docker/Cloud Run) emphasize the need to configure the MongoDB Atlas connection string correctly.  Critically, the Cloud Run example now shows how to pass the connection string as an environment variable using the `--set-env-vars` flag.
*   **Security Note:** A warning has been added about the importance of not committing sensitive connection strings directly into the repository. Using environment variables is the recommended best practice.
*    **IP WhiteList:** An info to add atlas config and IP Whitelist.
*   **Deployment Details:**  Minor clarifications in the Cloud Run deployment steps.
*   **Restarting the Service:**  A note has been added indicating that updating environment variables via `gcloud run services update` will restart the service.
*   **Interview Playback:** The "Next Steps" section mentions discussing the challenges and solutions related to MongoDB Atlas configuration and the data seeder.
* ****
This version is much more robust and provides clear instructions for configuring and deploying the application with MongoDB Atlas on GCP Cloud Run.  It also highlights the importance of security best practices. Remember to replace the placeholder connection string with your actual MongoDB Atlas connection string.