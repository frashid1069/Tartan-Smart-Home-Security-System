# Tartan Smart Home Security
## Members:

#### Armaan Das (adas5)

#### Farhan Rashid (frashid2)

#### Lingfeng Zhu (lingfen1)

#### Michael Shi (wenjian2)



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

### Automated Deployment with GitHub Actions
Deployment is automated via **GitHub Actions**. When you push to any branch, the workflow is triggered to build, test, and deploy your services. The process is divided into the following stages:

1. **Build and Test:**
   - The code is checked out and built using Gradle.
   - Unit tests and Jacoco code coverage reports are generated.

2. **Build and Push Docker Images:**
   - Docker Compose is used to build the Docker images for each service.
   - The images are pushed to **Docker Hub** using the login credentials stored in GitHub Secrets.

3. **Deploy:**
   - The deployment process will be triggered via the **deploy** job in the GitHub Actions workflow.
   - The deployment script (`deployment.sh`) will clone the repository (if not already cloned) and execute the `docker-compose` commands to pull and run the latest containers.
  
### Manual Deployment Instructions (if not using GitHub Actions)

If you need to manually deploy or update the containers, follow these steps:

#### Launch Containers:
- **Build the Docker Images:**
    ```bash
    docker-compose -f smart-home/docker-compose.yml build
    ```
- **Start Containers:**
    ```bash
    docker-compose -f smart-home/docker-compose.yml up -d
    ```

#### Update Containers:
- **Pull the Latest Container Versions:**
    ```bash
    docker-compose -f smart-home/docker-compose.yml pull
    ```
- **Re-deploy the Containers (update the containers with the new images):**
    ```bash
    docker-compose -f smart-home/docker-compose.yml down
    docker-compose -f smart-home/docker-compose.yml up -d
    ```

#### Rollback Containers:
- **Rollback to the Previous Version:**
    ```bash
    ./deployment.sh rollback
    ```
- **Rollback to a Specific Version (e.g., v1.0.0):**
    ```bash
    ./deployment.sh rollback v1.0.0
    ```
- **Rollback a Specific Service (e.g., platform):**
    ```bash
    ./deployment.sh rollback-service platform
    ```

### Deployment to Cybera VM:
The deployment to the Cybera VM is handled automatically by the `deploy` job. The steps are as follows:
- The deployment script (`deployment.sh`) checks if the repository is already cloned on the target server.
- If not, it clones the repository from GitHub.
- The script then runs the latest containers using `docker-compose up -d`.

For more details, check the `deployment.sh` file and **GitHub Actions** workflows.

### SSH Key Setup for Deployment
To allow the deployment to run on the Cybera VM, you need to ensure that the server has an SSH key set up to authenticate the GitHub Actions runner:
- The SSH key is stored in GitHub Secrets (`SSH_PEM_KEY`), and the key is used to authenticate the connection to the target server.
