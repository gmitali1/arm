# Choose a base image
FROM openjdk:18-jdk-alpine

# Install necessary packages
RUN apk add --no-cache nodejs npm
RUN apk add --no-cache maven

# Copy the necessary files
COPY /src/main/js/ecommerce /app
COPY /jars /app/jars
COPY start-jars.sh /app

# Set the working directory
WORKDIR /app

# Build the Angular app
RUN npm install
RUN npm install --save-dev copyfiles
RUN npm run build
RUN npm install -g http-server


# Expose ports
EXPOSE 8080
EXPOSE 9090
EXPOSE 9091
EXPOSE 9092
EXPOSE 9093
EXPOSE 9094
EXPOSE 80
EXPOSE 4200

# Specify the command to run
#CMD ["http-server", "dist/frontend", "-p4200", " > logserver.log"]
CMD ["sh", "start-jars.sh"]
