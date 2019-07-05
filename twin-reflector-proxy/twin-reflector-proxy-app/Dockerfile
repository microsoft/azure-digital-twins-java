FROM mcr.microsoft.com/java/jre-headless:11u3-zulu-alpine

ARG JAR_FILE=twin-reflector-proxy-app-0.1.0-SNAPSHOT.jar

ENV REFLECTOR_HOME=/opt/reflector

EXPOSE 8080

VOLUME "$REFLECTOR_HOME/data"

WORKDIR $REFLECTOR_HOME
ENTRYPOINT ["java","-jar","twin-reflector-proxy-app.jar","-Xmx768m -Xmx768m -XX:MaxMetaspaceSize=250m -XX:MetaspaceSize=250m -Xss300K -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+HeapDumpOnOutOfMemoryError"]

# Add the service itself
ADD target/$JAR_FILE twin-reflector-proxy-app.jar
