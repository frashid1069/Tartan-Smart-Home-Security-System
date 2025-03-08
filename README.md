# Group 13 - Lab H01
## Members:

#### Armaan Das (adas5)

#### Farhan Rashid (frashid2)

#### Lingfeng Zhu (lingfen1)

#### Michael Shi (wenjian2)


# Tartan

## Building

The build instructions can be found [here](./docs/build_instructions.md).

## System description

System description can be downloaded as a pdf file
[here](./docs/TartanSystemDescription.pdf).

## Folder structure

The entire system (Tartan Smart Home Service) resides in *smart-home/*
directory.

Please see the system description (docx) file for more detailed information
about Tartan's design, architecture, requirements, etc.

## Deployment Instructions

### Launch Containers:
- **Build the Docker Images:** docker-compose -f smart-home/docker-compose.yml build
- **Start Containers:** docker-compose -f smart-home/docker-compose.yml up -d

### Update Containers:
- **Pull the Latest Container Versions:** docker-compose -f smart-home/docker-compose.yml pull
- **Re-deploy the Containers (update the containers with the new images):**
    - docker-compose -f smart-home/docker-compose.yml down
    - docker-compose -f smart-home/docker-compose.yml up -d

### Rollback Containers:
- **Rollback to the Previous Version:** ./deployment.sh rollback
- **Rollback to a Specific Version:** ./deployment.sh rollback v1.0.0
- **Rollback a Specific Service:** ./deployment.sh rollback-service platform

### Deployment Workflow:
The deployment process is automated via GitHub Actions. When you push to any branch, the workflow is triggered, and the following steps occur:

- Build and Test:
  - The code is checked out and built using Gradle.
  - Unit tests and reports (e.g., Jacoco) are generated.

- Build and Push Docker Images:
  - Docker images for each service are built.
  - The images are pushed to Docker Hub for remote usage.

- Deploy Containers:
  - The deployment script checks the connectivity (IPv6, then IPv4 if needed).
  - SSH is used to access the server, pull the latest code, and deploy the containers using Docker Compose.

- Rollback Containers (if necessary):
  - If needed, containers can be reverted to a previous version using the rollback functionality.
