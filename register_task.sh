#!/bin/bash

aws ecs register-task-definition --family assetapi --cli-input-json file://$(pwd)/task.json

