FROM tomee:8-jre-1.7.5-plus
MAINTAINER Team A

# Add the war file(s)
ADD target/orderservice-0.1.war /usr/local/tomee/webapps/
ADD tomcat-users.xml /usr/local/tomee/conf

EXPOSE 8080
CMD ["catalina.sh", "run"]
