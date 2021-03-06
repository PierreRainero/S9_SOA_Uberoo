#!/usr/bin/env bash
# -- Hostnames ---
coursierservice="localhost:9888"
orderService="localhost:9555"
restaurant="localhost:9777"
# ******** Context *********
echo "Scenario#10: As Terry, I want to get some statistics (speed, cost) about global delivery time and delivery per coursier."
echo ""
echo "--- Creating context... ---"

# Create Gail
echo "Create consumer : Gail"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/10/0_gailId.txt
cat temp/10/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/10/0_gailId.txt | head -1)
echo "Press any key to continue ..."
read

# Create Jamie
echo "Create a coursier : Jamie"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"name\":\"Jamie\",\"latitude\":10,\"longitude\":10,\"accountNumber\":\"FR89 3704 0044 0532 0130 00\"}" "http://$coursierservice/coursiers" > temp/10/8_jamieId.txt
jamie_id=$(grep -Po '"id": *\K[^,]*' temp/10/8_jamieId.txt | head -1)
cat temp/10/8_jamieId.txt
echo "Press any key to continue ..."
read

# Create a restaurant
echo "Create a restaurant : Asiakeo"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"longitude\":9.7,\"latitude\":9.7,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/10/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/10/1_restaurantCreated.txt | head -1)
cat temp/10/1_restaurantCreated.txt
echo "Press any key to continue ..."
read

# Create a ramen meal POST /restaurants/{restaurantId}/meals
echo "Create a meal : Ramen"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/10/2_mealCreated.txt
cat temp/10/2_mealCreated.txt
echo "Press any key to continue ..."
read

# Récupère les restaurants disponibles
curl -X GET --silent "http://$orderService/restaurants" > temp/10/3_resultOfRestaurants.txt
restau_id=$(grep -Po '"id": *\K[^,]*' temp/10/3_resultOfRestaurants.txt | tail -1)

# Choisit son plat et envoie la commande
echo "Create an order"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/10/5_orderWithETA.txt
cat temp/10/5_orderWithETA.txt
sed -i 's/WAITING/VALIDATED/g' temp/10/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/10/5_orderWithETA.txt | head -1)
echo "Press any key to continue..."
read

# Envoi au système, le système poste un message dans le bus pour le restaurant :
echo "Validate the ETA of the new order"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/10/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/10/6_validatedOrder.txt
cat temp/10/6_validatedOrder.txt
echo "Press any key to continue ..."
read


# Récupère les commandes qui doivent etre délivré autour de Jamie
curl -X GET --silent "http://$coursierservice/deliveries/?latitude=9.7&longitude=9.7" > temp/10/7_resultOfDeliveries.txt
delivery_id=$(grep -Po '"id": *\K[^,]*' temp/10/7_resultOfDeliveries.txt | head -2 | tail -1)

#Jamie takes this order
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{}" "http://$coursierservice/deliveries/$delivery_id/?coursierId=$jamie_id" > temp/10/9_resultTakeOrder.txt
echo "Jamie takes this order"
cat temp/10/9_resultTakeOrder.txt
echo "Press any key to continue..."
read

docker exec soa_database mysql -uroot -pteama -e "USE uberoo_coursierService; select id from RESTAURANT where name = 'Asiakeo';" > temp/10/resto.txt
restaurant_id=$(cat temp/10/resto.txt | tail -1)
#Jamie delivers the order
echo "Jamie delivers the order"
curl -X GET --silent "http://$coursierservice/deliveries" > temp/10/10_pendingDeliveries.txt
sed -i 's/"state":false/"state":true/g' temp/10/10_pendingDeliveries.txt
json=$(cat temp/10/10_pendingDeliveries.txt)
json=${json:1: -1}
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$json" "http://$coursierservice/deliveries" > temp/10/11_resultDeliverOrder.txt
cat temp/10/11_resultDeliverOrder.txt
echo "Press any key to continue..."
read

echo "--- Context created ---"
echo ""
# ******* Scénario *********

echo "*******1- As Terry, I want to know the statistics of Jamie"
curl -X GET --silent "http://$coursierservice/coursiers/$jamie_id/deliveries/?idRestaurant=$restaurant_id" > temp/10/12_resultStatistics.txt
cat temp/10/12_resultStatistics.txt
echo "Press any key to continue..."
read