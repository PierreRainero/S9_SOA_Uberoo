#!/usr/bin/env bash
echo "As Jamie, I want to inform quickly that I can't terminate the course (accident, sick), so that the order can be replaced."

# WIP:-- HostNames ---
coursierservice="localhost:9888"
orderService="localhost:9555"
restaurant="localhost:9777"
# ******** Context *********
echo "--- Creating context ... ---"

# Create gaile
echo "Create consumer Gaile"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/12/0_gailId.txt
cat temp/12/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/12/0_gailId.txt | head -1)
echo "Press any key to continue ..."
read

# Create Jamie
echo "Create Jamie the coursier"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"name\":\"Jamie\",\"latitude\":12,\"longitude\":12,\"accountNumber\":\"FR89 3704 0044 0532 0130 00\"}" "http://$coursierservice/coursiers" > temp/12/8_jamieId.txt
jamie_id=$(grep -Po '"id": *\K[^,]*' temp/12/8_jamieId.txt | head -1)
cat temp/12/8_jamieId.txt
echo "Press any key to continue ..."
read

# Create a restaurant
echo "Create a restaurant"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"longitude\":9.7,\"latitude\":9.7,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/12/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/12/1_restaurantCreated.txt | head -1)
cat temp/12/1_restaurantCreated.txt
echo "Press any key to continue ..."
read

# Create a ramen meal POST /restaurants/{restaurantId}/meals
echo "Create a ramen meal POST"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/12/2_mealCreated.txt
cat temp/12/2_mealCreated.txt
echo "Press any key to continue ..."
read

# Récupère les restaurants disponibles
echo "Récupère les restaurants disponibles"
curl -X GET --silent "http://$orderService/restaurants" > temp/12/3_resultOfRestaurants.txt
cat temp/12/3_resultOfRestaurants.txt
echo "Press any key to continue ..."
read
restau_id=$(grep -Po '"id": *\K[^,]*' temp/12/3_resultOfRestaurants.txt | tail -1)

# Récupère tous les plats d'un restaurant spécifique
echo "Récupère tous les plats d'un restaurant spécifique"
curl -X GET --silent "http://$orderService/restaurants/$restau_id/meals" > temp/12/4_resultOfMeals.txt
cat temp/12/4_resultOfMeals.txt
echo "Press any key to continue ..."
read

# Choisit son plat et envoie la commande
echo "Choisit son plat"
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/12/5_orderWithETA.txt
cat temp/12/5_orderWithETA.txt
sed -i 's/WAITING/VALIDATED/g' temp/12/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/12/5_orderWithETA.txt | head -1)
echo "Press any key to continue..."
read

# Envoit au système, le système poste un message dans le bus pour le restaurant :
echo "Envoit au système"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/12/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/12/6_validatedOrder.txt
cat temp/12/6_validatedOrder.txt
echo "Press any key to continue ..."
read

# Récupère les commandes qui doivent etre délivré autour de Jamie
echo "Récupère les commandes qui doivent être livrées"
curl -X GET --silent "http://$coursierservice/deliveries/?latitude=9.7&longitude=9.7" > temp/12/7_resultOfDeliveries.txt
echo "Result (temp/12/7_resultOfDeliveries.txt):"
cat temp/12/7_resultOfDeliveries.txt
delivery_id=$(grep -Po '"id": *\K[^,]*' temp/12/7_resultOfDeliveries.txt | head -1)
echo "Press any key to continue..."
read

# Jamie takes this order
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{}" "http://$coursierservice/deliveries/$delivery_id/?coursierId=$jamie_id" > temp/12/9_resultTakeOrder.txt
echo "Jamie takes this order"
cat temp/12/9_resultTakeOrder.txt
echo "Press any key to continue..."
read

echo "Jamie inform that he can't terminate the course"
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"coursierId\":$jamie_id,\"deliveryId\":$delivery_id,\"reasonType\":\"Panne\",\"explanations\":\"Panne du moteur\"}" "http://$coursierservice/deliveries/$delivery_id/?coursierId=$jamie_id" > temp/12/10_resultIssue.txt
cat temp/12/10_resultIssue.txt
echo "Press any key to continue..."
read