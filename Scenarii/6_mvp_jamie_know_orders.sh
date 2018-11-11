#!/usr/bin/env bash
coursierservice="localhost:9888"
orderService="localhost:9555"
restaurant="localhost:9777"
# ******** Context *********
echo "Scenario#5: As Jamie, I want to know the orders that will have to be delivered around me, so that I can choose one and go to the restaurant to begin the course."
echo ""
echo "--- Creating context... ---"

# Create Gail
echo "Create consumer : Gail"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/6/0_gailId.txt
cat temp/6/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/6/0_gailId.txt | head -1)
echo "Press any key to continue ..."
read
# Create Jamie
echo "Create a coursier : Jamie"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"name\":\"Jamie\",\"latitude\":10,\"longitude\":10,\"accountNumber\":\"FR89 3704 0044 0532 0130 00\"}" "http://$coursierservice/coursiers" > temp/6/8_jamieId.txt
cat temp/6/8_jamieId.txt
echo "Press any key to continue ..."
read
# Create a restaurant
echo "Create a restaurant : Asiakeo"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/6/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/6/1_restaurantCreated.txt | head -1)
cat temp/6/1_restaurantCreated.txt
echo "Press any key to continue ..."
read

# Create a ramen meal POST /restaurants/{restaurantId}/meals
echo "Create a meal : Ramen"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/6/2_mealCreated.txt
cat temp/6/2_mealCreated.txt
echo "Press any key to continue ..."
read

# Récupère les restaurants disponibles
curl -X GET --silent "http://$orderService/restaurants" > temp/6/3_resultOfRestaurants.txt
restau_id=$(grep -Po '"id": *\K[^,]*' temp/6/3_resultOfRestaurants.txt | tail -1)

# Choisit son plat et envoie la commande
echo "Create an order"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/6/5_orderWithETA.txt
cat temp/6/5_orderWithETA.txt
sed -i 's/WAITING/VALIDATED/g' temp/6/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/6/5_orderWithETA.txt | head -1)
echo "Press any key to continue..."
read

# Envoi au système, le système poste un message dans le bus pour le restaurant :
echo "Validate the ETA of the new order"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/6/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/6/6_validatedOrder.txt
cat temp/6/6_validatedOrder.txt
echo "Press any key to continue ..."
read
echo "--- Context created ---"
echo ""


# ******* Scénario *********

# Récupère les commandes qui doivent etre délivré autour de Jamie
echo "*******1- As Jamie, I search all the orders to deliver"
curl -X GET --silent "http://$coursierservice/deliveries/?latitude=9.7&longitude=9.7" > temp/6/7_resultOfDeliveries.txt
echo "Result (temp/6/7_resultOfDeliveries.txt):"
cat temp/6/7_resultOfDeliveries.txt
echo ""
echo ""
echo "Press any key to continue..."
read