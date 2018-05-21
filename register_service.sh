#!/bin/bash

aws ecs create-service --cluster production --service-name assetapi-production --task-definition assetapi --desired-count 1 --launch-type "FARGATE" --network-configuration "awsvpcConfiguration={subnets=[subnet-da20caa0],securityGroups=[sg-62b77908]}"

