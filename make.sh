docker build ./conf/postgres/ -t postgres-tasks
sbt docker:publishLocal
docker-compose up -d