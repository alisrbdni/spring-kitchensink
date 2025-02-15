# Spring Kitchensink

This project is a Spring Boot + MongoDB application that can be deployed locally using Docker or on Google Cloud Platform (GCP) using Terraform & Cloud Run.

## Application URL

The application is deployed on GCP Cloud Run and is available at:
- [Classic UI](https://kitchensink-93677313045.us-central1.run.app/members-ui)

## Test Files

The project includes unit tests to ensure the functionality of the application. Below is a list of key test files:

- `spring/kitchensink/src/test/java/com/example/kitchensink/service/MemberServiceTest.java`: Contains unit tests for the `MemberService` class, testing member registration, email uniqueness, and retrieval operations.
- `spring/kitchensink/src/test/java/com/example/kitchensink/repository/MemberRepositoryTest.java`: Contains unit tests for the `MemberRepository` class, testing CRUD operations.

## Running Locally with Docker

### Prerequisites

Ensure you have the following installed:
- Docker
- Maven
- Java 21 JDK

### Steps to Run Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
   cd kitchensink
   ```

2. **Build the JAR file**:
   ```bash
   mvn clean package
   ```

3. **Build the Docker image**:
   ```bash
   docker build -t kitchensink:latest .
   ```

4. **Run the Docker container**:
   ```bash
   docker run -p 8080:8080 kitchensink:latest
   ```

5. **Access the application**:
   - Classic UI: [http://localhost:8080/members-ui](http://localhost:8080/members-ui)
   - New UI: [http://localhost:8080/new-ui](http://localhost:8080/new-ui)
   - Test API Endpoint: [http://localhost:8080/api/members](http://localhost:8080/api/members)

## Deploying to GCP using Terraform

### Prerequisites

Ensure you have the following installed:
- Google Cloud SDK (gcloud CLI)
- Terraform

### Enable Required GCP Services

Run the following commands to enable required APIs:
```bash
gcloud services enable \
    compute.googleapis.com \
    run.googleapis.com \
    cloudbuild.googleapis.com \
    artifactregistry.googleapis.com
```

### Steps to Deploy

1. **Authenticate & Set Project**:
   ```bash
   gcloud auth application-default login
   gcloud config set project YOUR_GCP_PROJECT_ID
   ```

2. **Navigate to Terraform directory**:
   ```bash
   cd kitchensink/terraform
   ```

3. **Initialize Terraform**:
   ```bash
   terraform init
   ```

4. **Apply Terraform Configuration**:
   ```bash
   terraform apply -var="gcp_project_id=YOUR_PROJECT_ID" -auto-approve
   ```

5. **Retrieve Cloud Run URL**:
   ```bash
   terraform output cloud_run_url
   ```

### (Optional) Build & Push Docker Image Manually

If Terraform does not handle Docker builds, manually build and push the image:
```bash
docker build -t us-central1-docker.pkg.dev/YOUR_PROJECT_ID/kitchensink-repo/kitchensink:latest .
docker push us-central1-docker.pkg.dev/YOUR_PROJECT_ID/kitchensink-repo/kitchensink:latest
```

## Verify Cloud Deployment

### Open in Browser

```bash
open $(terraform output -raw cloud_run_url)
```

### Test API with cURL

```bash
curl $(terraform output -raw cloud_run_url)/api/members
```

### Check the Web UI

- Classic UI: [https://kitchensink-app-xyz123.a.run.app/members-ui](https://kitchensink-app-xyz123.a.run.app/members-ui)
- New UI: [https://kitchensink-app-xyz123.a.run.app/new-ui](https://kitchensink-app-xyz123.a.run.app/new-ui)

## Cleanup (Destroy GCP Resources)

To delete all resources created by Terraform:
```bash
terraform destroy -var="gcp_project_id=YOUR_PROJECT_ID" -auto-approve
```

## Summary

- **Local Docker Run**: ✅
- **Terraform GCP Deploy**: ✅
- **Spring Boot App**: Runs in Docker locally and deploys to Cloud Run
- **Exposed on Port 8080**: ✅ localhost:8080, ✅ Public Cloud Run URL
- **API Access**: ✅ localhost:8080/api/members, ✅ /api/members on Cloud Run
- **Auto Build & Deploy**: ❌ Manual, ✅ Automated with Cloud Build

🚀 Now, your kitchensink Spring Boot app can run locally and on GCP! 🚀
