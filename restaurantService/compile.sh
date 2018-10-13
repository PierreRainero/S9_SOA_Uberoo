#!/bin/bash

mvn clean package

docker build -t "soa/wb/restaurantservice" .