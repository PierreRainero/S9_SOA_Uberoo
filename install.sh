#!/bin/bash

#echo "*******Building Docker image for the message bus*******"
#cd ./messageBus
#./compile.sh
echo "*******Building Docker image for the order service*******"
cd orderService
./compile.sh
echo "*******Building Docker image for the restaurant service*******"
cd ../restaurantService
./compile.sh
echo "*******Building Docker image for the coursier service*******"
cd ../coursierService
./compile.sh
echo "*******Installation complete*******"

# echo "Press enter to close"
# read