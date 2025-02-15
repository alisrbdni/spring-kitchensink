# spring-kitchensink


Spring Boot Kitchensink App - Deploy with Docker & Terraform on GCP

This project is a Spring Boot + MongoDB application that can be:

Run locally using Docker

Deployed to Google Cloud Platform (GCP) using Terraform & Cloud Run

1️⃣ Running Locally with Docker

To run the application locally using Docker, follow these steps.

🔹 Prerequisites

Make sure you have the following installed:

Docker → Install Docker

Maven → Install Maven

Java 21 JDK → Install Java 21

🔹 Steps to Run Locally

Clone the repository:

git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd kitchensink

Build the JAR file:

mvn clean package

This will generate target/spring-kitchensink-1.0.0-SNAPSHOT.jar.

Build the Docker image:

docker build -t kitchensink:latest .

Run the Docker container:

docker run -p 8080:8080 kitchensink:latest

Access the application:

Open Classic UI: http://localhost:8080/members-ui

Open New UI: http://localhost:8080/new-ui

Test API Endpoint: http://localhost:8080/api/members

2️⃣ Deploying to GCP using Terraform

This section explains how to deploy the app on GCP Cloud Run using Terraform.

🔹 Prerequisites

Make sure you have the following installed:

Google Cloud SDK (gcloud CLI) → Install gcloud

Terraform → Install Terraform

🔹 Enable Required GCP Services

Run the following commands to enable required APIs:

gcloud services enable \
    compute.googleapis.com \
    run.googleapis.com \
    cloudbuild.googleapis.com \
    artifactregistry.googleapis.com

🔹 Steps to Deploy

Authenticate & Set Project:

gcloud auth application-default login
gcloud config set project YOUR_GCP_PROJECT_ID

Navigate to Terraform directory:

cd kitchensink/terraform

Initialize Terraform:

terraform init

Apply Terraform Configuration:

terraform apply -var="gcp_project_id=YOUR_PROJECT_ID" -auto-approve

This will:

Create an Artifact Registry to store the Docker image.

Deploy the Spring Boot application to Cloud Run.

Output the public URL of the app.

Retrieve Cloud Run URL:

terraform output cloud_run_url

(Optional) Build & Push Docker Image Manually
If Terraform does not handle Docker builds, manually build and push the image:

docker build -t us-central1-docker.pkg.dev/YOUR_PROJECT_ID/kitchensink-repo/kitchensink:latest .
docker push us-central1-docker.pkg.dev/YOUR_PROJECT_ID/kitchensink-repo/kitchensink:latest

3️⃣ Verify Cloud Deployment

Once the deployment is complete, verify the app is accessible.

🔹 Open in Browser

open $(terraform output -raw cloud_run_url)

or manually visit the URL.

🔹 Test API with cURL

curl $(terraform output -raw cloud_run_url)/api/members

🔹 Check the Web UI

Classic UI: https://kitchensink-app-xyz123.a.run.app/members-ui

New UI: https://kitchensink-app-xyz123.a.run.app/new-ui

4️⃣ Cleanup (Destroy GCP Resources)

To delete all resources created by Terraform:

terraform destroy -var="gcp_project_id=YOUR_PROJECT_ID" -auto-approve

This will:
✔ Remove the Cloud Run Service✔ Delete the Docker Image from Artifact Registry✔ Clean up IAM permissions

5️⃣ Summary

Feature

Local Docker Run ✅

Terraform GCP Deploy ✅

Spring Boot App

✅ Runs in Docker locally

✅ Deploys to Cloud Run

Exposed on Port 8080

✅ localhost:8080

✅ Public Cloud Run URL

API Access

✅ localhost:8080/api/members

✅ /api/members on Cloud Run

Auto Build & Deploy

❌ Manual

✅ Automated with Cloud Build

🚀 Now, your kitchensink Spring Boot app can run locally and on GCP! 🚀