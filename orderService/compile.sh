#!/bin/bash

mvn clean install

docker build -t "soa/wb/orderservice" .