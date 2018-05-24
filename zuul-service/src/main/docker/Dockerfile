FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /opt/ivops/tmp
ADD zuul-service-1.0.0-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=peer2"]
#EXPOSE 8761