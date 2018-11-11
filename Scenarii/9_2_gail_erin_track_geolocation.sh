#!/usr/bin/env bash
# -- Hostnames ---
coursierservice="localhost:9888"
orderService="localhost:9555"
restaurant="localhost:9777"
# ******** Context *********
echo "Scenario#9: As a customer (Gail, Erin), I want to track the geolocation of the coursier in real time, so that I can anticipate when I will eat."
echo ""
echo "--- Creating context... ---"

# Create Gail
echo "Create consumer : Gail"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/9/0_gailId.txt
cat temp/9/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/9/0_gailId.txt | head -1)
echo "Press any key to continue ..."
read

# Create Jamie
echo "Create a coursier : Jamie"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"name\":\"Jamie\",\"latitude\":9,\"longitude\":9,\"accountNumber\":\"FR89 3704 0044 0532 0130 00\"}" "http://$coursierservice/coursiers" > temp/9/8_jamieId.txt
jamie_id=$(grep -Po '"id": *\K[^,]*' temp/9/8_jamieId.txt | head -1)
cat temp/9/8_jamieId.txt
echo "Press any key to continue ..."
read

# Create a restaurant
echo "Create a restaurant : Asiakeo"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"longitude\":9.7,\"latitude\":9.7,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/9/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/9/1_restaurantCreated.txt | head -1)
cat temp/9/1_restaurantCreated.txt
echo "Press any key to continue ..."
read

# Create a ramen meal POST /restaurants/{restaurantId}/meals
echo "Create a meal : Ramen"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/9/2_mealCreated.txt
cat temp/9/2_mealCreated.txt
echo "Press any key to continue ..."
read

# Récupère les restaurants disponibles
curl -X GET --silent "http://$orderService/restaurants" > temp/9/3_resultOfRestaurants.txt
restau_id=$(grep -Po '"id": *\K[^,]*' temp/9/3_resultOfRestaurants.txt | tail -1)

# Choisit son plat et envoie la commande
echo "Create an order"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 0649 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/9/5_orderWithETA.txt
cat temp/9/5_orderWithETA.txt
sed -i 's/WAITING/VALIDATED/g' temp/9/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/9/5_orderWithETA.txt | head -1)
echo "Press any key to continue..."
read

# Envoi au système, le système poste un message dans le bus pour le restaurant :
echo "Validate the ETA of the new order"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/9/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/9/6_validatedOrder.txt
cat temp/9/6_validatedOrder.txt
echo "Press any key to continue ..."
read
echo "--- Context created ---"
echo ""
# ******* Scénario *********

# Récupère les commandes qui doivent etre délivré autour de Jamie
echo "*******1- As Jamie, I search all the orders to deliver"
curl -X GET --silent "http://$coursierservice/deliveries/?latitude=9.7&longitude=9.7" > temp/9/7_resultOfDeliveries.txt
echo "Result (temp/9/7_resultOfDeliveries.txt):"
cat temp/9/7_resultOfDeliveries.txt
delivery_id=$(grep -Po '"id": *\K[^,]*' temp/9/7_resultOfDeliveries.txt | head -2 | tail -1)
echo ""
echo ""
echo "Press any key to continue..."
read

#Jamie takes this order
echo "*******2- As Jamie, I decide to take this delivery"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{}" "http://$coursierservice/deliveries/$delivery_id/?coursierId=$jamie_id" > temp/9/9_resultTakeOrder.txt
cat temp/9/9_resultTakeOrder.txt
echo ""
echo ""
echo "Press any key to continue..."
read

echo "*******3- As Gail, I want to know where Jamis is"
curl -X GET --silent "http://$coursierservice/coursiers/$jamie_id" > temp/9/10_resultCoursier.txt
cat temp/9/10_resultCoursier.txt
echo ""
echo ""
echo "Press any key to continue..."
read