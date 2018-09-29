# Database MySQL

docker run -d -p 3306:3306 --name uberoo-db \
-e MYSQL_ROOT_PASSWORD=teama \
-e MYSQL_DATABASE=uberoo \
mysql

# To check
# docker exec -it uberoo-db bash
# mysql -uroot -p
# show databases;
# use uberoo;
# show tables;