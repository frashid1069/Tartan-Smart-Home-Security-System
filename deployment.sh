#!/bin/bash
# Pull and run the latest containers
if [ "$1" == "latest" ]; then
    echo "Deploying the latest containers..."
    docker-compose down
    docker-compose pull
    docker-compose up -d
    echo "Deployment successful!"

# Rollback to the previous version
elif [ "$1" == "rollback" ]; then
    ROLLBACK_VERSION="${2:-previous}"
    echo "Rolling back to version: $ROLLBACK_VERSION..."
    docker-compose down
    docker-compose pull "${DOCKERHUB_USERNAME}/platform:${ROLLBACK_VERSION}"
    docker-compose up -d
    echo "Rollback to version $ROLLBACK_VERSION successful!"

# Rollback specific service
elif [ "$1" == "rollback-service" ]; then
    if [ -z "$2" ]; then
        echo "Please specify the service to rollback."
        exit 1
    fi
    echo "Rolling back service $2..."
    docker-compose down "$2"
    docker-compose pull "$2"
    docker-compose up -d "$2"
    echo "Rollback for service $2 completed!"

else
    echo "Usage: ./deploy.sh {latest|rollback|rollback-service <service_name>}"
    exit 1
fi