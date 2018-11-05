#!/usr/bin/env bash
echo "As Jamie, I want to know the orders that will have to be delivered around me, so that I can choose one and go to the restaurant to begin the course."
# -- HostNames ---
coursierservice="localhost:9888"
orderService="localhost:9555"
restaurant="localhost:9777"
# ******** Context *********
echo "--- Creating context ... ---"

# Create gaile
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/6/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/6/0_gailId.txt | head -1)

# Create a restaurant
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/6/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/6/1_restaurantCreated.txt | head -1)

# Create a ramen meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/6/2_mealCreated.txt
sleep 2

# Récupère les restaurants disponibles
curl -X GET --silent "http://$orderService/restaurants" > temp/6/3_resultOfRestaurants.txt
restau_id=$(grep -Po '"id": *\K[^,]*' temp/6/3_resultOfRestaurants.txt | tail -1)

# Récupère tous les plats d'un restaurant spécifique
curl -X GET --silent "http://$orderService/restaurants/$restau_id/meals" > temp/6/4_resultOfMeals.txt

# Choisit son plat et envoie la commande
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restau_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/6/5_orderWithETA.txt

# ******* Scénario *********

# Récupère les commandes qui doivent etre délivré autour de Jamie
curl -X GET --silent -H "http://$coursierservice/deliveries" > temp/6/6_resultOfDeliveries.txt
echo "Result (temp/6/6_resultOfDeliveries.txt):"
cat temp/6/6_resultOfDeliveries.txt
echo "Press any key to continue..."
read
sleep 2