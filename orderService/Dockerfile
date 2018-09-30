FROM tomee:8-jre-1.7.5-plus
MAINTAINER Team A

# Add the war file(s)
ADD target/orderservice-0.1.war /usr/local/tomee/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
