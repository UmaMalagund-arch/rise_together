FROM tomcat:9-jdk17
LABEL maintainer="umamalagund635@gmail.com"

# Remove default ROOT folder
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your generated WAR file from target folder
COPY target/rise_together.war /usr/local/tomcat/webapps/ROOT.war

# Custom port (NOT 8080)
EXPOSE 8084

# Start Tomcat
CMD ["catalina.sh", "run"]
