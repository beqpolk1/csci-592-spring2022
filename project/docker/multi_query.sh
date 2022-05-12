#!/bin/bash

echo -n "Enter query set (1 or 2): "
read querySet

docker run -it -v /mnt/c/Users/Ben/Repositories/csci-592-spring2022/project/MultiQuery/out/artifacts/MultiQuery_jar:/mnt csci-592/env java -cp MultiQuery.jar TestQueryRunner $querySet