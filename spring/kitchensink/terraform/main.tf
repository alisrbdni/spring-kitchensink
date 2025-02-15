terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

variable "gcp_project_id" {
  description = "GCP Project ID"
  type        = string
}

variable "gcp_region" {
  description = "GCP Region"
  type        = string
  default     = "us-central1"
}

variable "service_name" {
  description = "Cloud Run Service Name"
  type        = string
  default     = "kitchensink-app"
}

variable "artifact_repo_name" {
  description = "Artifact Registry Repository Name"
  type        = string
  default     = "kitchensink-repo"
}

# Create Artifact Registry for Docker Images
resource "google_artifact_registry_repository" "docker_repo" {
  location      = var.gcp_region
  repository_id = var.artifact_repo_name
  format        = "DOCKER"
}

# Build & Push Docker Image using Cloud Build
resource "google_cloudbuild_trigger" "docker_build" {
  name     = "kitchensink-build"
  location = var.gcp_region

  github {
    owner = "YOUR_GITHUB_USERNAME"
    name  = "YOUR_REPOSITORY_NAME"
    push {
      branch = "main"
    }
  }

  filename = "cloudbuild.yaml"
}

# Deploy Cloud Run Service
resource "google_cloud_run_service" "cloud_run" {
  name     = var.service_name
  location = var.gcp_region

  template {
    spec {
      containers {
        image = "us-central1-docker.pkg.dev/${var.gcp_project_id}/${var.artifact_repo_name}/kitchensink:latest"
        ports {
          container_port = 8080
        }
      }
    }
  }

  traffic {
    percent = 100
    latest_revision = true
  }
}

# Allow unauthenticated access (optional)
resource "google_cloud_run_service_iam_policy" "public_access" {
  location = google_cloud_run_service.cloud_run.location
  service  = google_cloud_run_service.cloud_run.name

  policy_data = jsonencode({
    bindings = [{
      role    = "roles/run.invoker"
      members = ["allUsers"]
    }]
  })
}

output "cloud_run_url" {
  value = google_cloud_run_service.cloud_run.status[0].url
}
