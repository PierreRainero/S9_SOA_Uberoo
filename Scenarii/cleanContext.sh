echo "Cleaning context"
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_coursierService; DELETE FROM COURSIER;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_coursierService; DELETE FROM DELIVERY;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_coursierService; DELETE FROM Delivery_food;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_coursierService; DELETE FROM RESTAURANT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM FEEDBACK;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM MEAL;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM Meal_tags;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM PAYMENT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM RESTAURANT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM UBEROOORDER;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM UBEROOORDER_MEAL;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_orderService; DELETE FROM USER;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM FEEDBACK;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM INGREDIENT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM MEALS;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM MEALS_INGREDIENT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM Meal_tags;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM RESTAURANT;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM RESTAURANTORDER;SET FOREIGN_KEY_CHECKS=1;";
docker exec soa_database mysql -uroot -pteama -e "SET FOREIGN_KEY_CHECKS=0;USE uberoo_restaurantService; DELETE FROM RESTAURANTORDER_MEALS;SET FOREIGN_KEY_CHECKS=1;";
echo "Context cleaned"