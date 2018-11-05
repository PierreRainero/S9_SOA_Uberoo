#!/bin/bash
if [ $(uname -s) == "MINGW64_NT-10.0" ]
then
MSYS_NO_PATHCONV=1 docker run -it --rm -v ${PWD}/loadTesting/conf:/opt/gatling/conf \
-v ${PWD}/loadTesting/user-files:/opt/gatling/user-files \
-v ${PWD}/loadTesting/results:/opt/gatling/results \
denvazh/gatling:2.3.1
else
docker run -it --rm -v ${PWD}/loadTesting/conf:/opt/gatling/conf \
-v ${PWD}/loadTesting/user-files:/opt/gatling/user-files \
-v ${PWD}/loadTesting/results:/opt/gatling/results \
denvazh/gatling:2.3.1
fi