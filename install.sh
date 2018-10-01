#!/bin/bash

echo "*******Building Docker image for the database*******"
cd database
./compile.sh
echo "*******Building Docker image for the message bus*******"
cd ../messageBus
./compile.sh
echo "*******Building Docker image for the order service*******"
cd ../orderService
./compile.sh
echo "*******Installation complete*******"
# echo "Press enter to close"
# read