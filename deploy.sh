docker stop forum

docker rm forum

# shellcheck disable=SC2006
# shellcheck disable=SC2046
docker rmi forum

mvn clean package

mv forum-website-0.0.1-SNAPSHOT.jar ./forum-website-0.0.1-SNAPSHOT.jar

docker build -t forum .

docker-compose up -d

