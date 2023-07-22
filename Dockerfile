# Base image
FROM openjdk:20

# Port application will be available on
EXPOSE 9090

# Add application JAR file to the container
ADD target/MtlDataConvert-0.0.1-SNAPSHOT.jar MtlDataConvert-0.0.1-SNAPSHOT.jar

# Copy the trust store file to the container
COPY ./cacerts /usr/local/openjdk-20/lib/security/cacerts

# Specify the command to start the application
ENTRYPOINT ["java","-Djavax.net.ssl.trustStore=/usr/local/openjdk-20/lib/security/cacerts","-Djavax.net.ssl.trustStorePassword=changeit","-jar","/MtlDataConvert-0.0.1-SNAPSHOT.jar"]

# Define the TRUST_STORE_PATH as an environment variable
ENV TRUST_STORE_PATH=/usr/local/openjdk-20/lib/security/cacerts
