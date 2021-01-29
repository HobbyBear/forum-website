FROM java:8
MAINTAINER xch
ADD   forum-website-0.0.1-SNAPSHOT.jar  forum.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","forum.jar"]
