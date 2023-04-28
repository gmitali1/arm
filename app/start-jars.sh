/opt/openjdk-18/bin/java -Dserver.port=8080 -Dserver.host=localhost -jar jars/DistributedStoreCoordinator.jar > log8080.log &
/opt/openjdk-18/bin/java -Dserver.port=9090 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080 -jar jars/DistributedStoreServer.jar > log9090.log &
/opt/openjdk-18/bin/java -Dserver.port=9091 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080 -jar jars/DistributedStoreServer.jar > log9091.log &
/opt/openjdk-18/bin/java -Dserver.port=9092 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080 -jar jars/DistributedStoreServer.jar > log9092.log &
/opt/openjdk-18/bin/java -Dserver.port=9093 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080 -jar jars/DistributedStoreServer.jar > log9093.log &
/opt/openjdk-18/bin/java -Dserver.port=9094 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080 -jar jars/DistributedStoreServer.jar > log9094.log &
http-server dist/frontend -p4200