docker stop forum-website

docker rm forum-website

# shellcheck disable=SC2006
# shellcheck disable=SC2046
docker rmi forum-website

mvn clean package

mv ./target/forum-website-0.0.1-SNAPSHOT.jar ./forum-website-0.0.1-SNAPSHOT.jar

docker build -t forum-website .

docker-compose up -d

