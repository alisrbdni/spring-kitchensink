# Kitchensink - Modernization from JBoss to Spring Boot (Java 21) + MongoDB 🚀


🎯 **Target:** Java 21, Spring Boot, MongoDB
💾 **Source:** Originally from Red Hat’s JBoss EAP Quickstarts (kitchensink example), modernized to Spring Boot (Java 21) with MongoDB as the new database. The application now utilizes MongoDB Atlas with pre-populated data via a seeder script.

🌍 **Deployed with Docker on GCP Cloud Run:**
##
🔗 [https://kitchensink-93677313045.us-central1.run.app/members-ui](https://kitchensink-93677313045.us-central1.run.app/members-ui)
🎨 Includes both the classic UI and a new modern UI.

## 1️⃣ Background & Purpose

This repository demonstrates the modernization of the JBoss EAP kitchensink quickstart application into a Spring Boot (Java 21) application. It was developed as part of a Modernization Factory: Developer Candidate Challenge, showcasing how a legacy JBoss Java application can be transitioned to a modern architecture.

### 🏛 Original Legacy Application:

*   Based on the JBoss EAP kitchensink quickstart ([GitHub link](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink))
*   Utilized Jakarta EE (CDI, EJB, JAX-RS, JSF, etc.)
*   Deployed on a JBoss EAP application server

### 🚀 Modernized Application:

*   Built with Spring Boot (Java 21)
*   Uses MongoDB Atlas instead of an H2 relational database
*   🐳 Containerized with Docker for easy deployment
*   🎨 Features two UI options: Classic (legacy-like) and a new modern UI

### 🔄 Migration Steps Overview:

*   🗺 **Code Mapping:** Used repomix to analyze the original structure.
*   🌱 **Spring Boot Initialization:** Created a new Spring Boot base application.
*   🔧 **Service & Function Conversion:** Migrated Jakarta EE components to Spring Boot equivalents.
*   💾 **Database Migration:** Replaced H2 with MongoDB Atlas, including a data seeder script.
*   ✅ **Automated Testing:** Expanded JUnit test coverage.
*   📦 **Deployment:** Containerized the app with Docker and deployed on GCP Cloud Run.

### 🎤 Interview Playback:

During an assessment, I will:

*   🎬 Demo the running application
*   📜 Explain key code changes
*   💡 Discuss lessons learned

## 2️⃣ Migration Approach

### 🔁 Key Changes from Jakarta EE to Spring Boot:

*   🔹 CDI → `@Service`, `@Autowired`
*   🔹 JAX-RS → `@RestController`
*   🔹 JSF → Thymeleaf templates
*   🔹 EJB → Spring Beans (`@Transactional` where necessary)
*   🔹 Persistence Configuration: JPA (H2) replaced with MongoDB Atlas and Spring Data Mongo

### 📂 Database Migration to MongoDB Atlas:

*   🔄 Converted JPA Entities to Spring Data MongoDB Entities
*   ⚙️ Updated `application.properties` for MongoDB Atlas connection
*   📜 Implemented a seeder script for initial data population

### 🎨 UI Modernization:

*   🏛 Classic UI (`members-list.html`) preserves legacy layout
*   ✨ Modern UI (`new-ui.html`) features an improved, card-based design

## 3️⃣ Architecture Overview

🏛 **Legacy (JBoss EAP)** | 🚀 **Modern (Spring Boot)**
----------------------- | -----------------------
📦 Deploy as WAR/EAR | 📦 Single JAR (embedded Tomcat)
🌐 JAX-RS `@Path("/members")` | 🔗 REST via `@RestController("/api/members")`
🎭 JSF (`*.xhtml`) | 🎨 Thymeleaf (`*.html`)
⚙️ EJB, CDI, JPA | ⚙️ `@Service`, `@Autowired`, Spring Data (MongoDB)
🗄 H2/Relational DB | 🗄 MongoDB Atlas
⚙️ `persistence.xml` | ⚙️ `application.properties` (YAML supported)

### 📌 Main Components:

*   🚀 `SpringKitchensinkApplication.java`: Bootstraps the Spring Boot app
*   🔗 `MemberController`: REST API for members
*   🛠 `MemberService`: Handles business logic (EJB replacement)
*   💾 `MemberRepository`: Uses Spring Data MongoDB
*   🎭 `templates/members-list.html`: Classic UI
*   🎨 `templates/new-ui.html`: Modern UI
*   📜 `Seeder Script`: Loads initial data into MongoDB Atlas

## 4️⃣ Build & Run Locally

### ⚙️ Prerequisites:

*   ☕ Java 21 installed
*   📦 Maven installed
*   ☁️ MongoDB Atlas configured

### 📥 Clone the repository:

```bash
git clone https://github.com/<your-username>/spring-kitchensink.git
cd spring-kitchensink
```

### 🏗 Build & Run:

```bash
cd spring/kitchensink
mvn clean install
mvn spring-boot:run
```

### 🌐 Access the Application:

*   🏛 Classic UI: [http://localhost:8080/members-ui](http://localhost:8080/members-ui)
*   ✨ Modern UI: [http://localhost:8080/new-ui](http://localhost:8080/new-ui)
*   🔗 API: [http://localhost:8080/api/members](http://localhost:8080/api/members)

### 🔥 Key Takeaways

*   🚀 Spring Boot and MongoDB Atlas provide a modern, scalable alternative to JBoss EAP and relational databases.
*   🐳 Containerization with Docker simplifies deployment and portability.
*   ☁️ Cloud Run deployment enables easy serverless hosting.
*   ✅ Automated testing ensures a smooth transition.

## 📅 Playback Plan

*   🎬 Live demo of the deployed application
*   👨‍💻 Code walkthrough showcasing migration changes
*   💡 Discussion on challenges and solutions
*   🔍 Best practices in modernization projects
