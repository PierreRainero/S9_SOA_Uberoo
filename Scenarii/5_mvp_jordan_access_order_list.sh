# OK-- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
echo "--- Creating context... ---"
# Creating gail
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/5/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/5/0_gailId.txt | head -1)
# Create a restaurant POST /restaurants
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/5/1_restaurantCreated1.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/2/1_restaurantCreated1.txt | head -1)
# Create a ramen and burger meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/5/2_mealCreated1.txt
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Maxi mega burger of the death\",\"price\":29,\"ingredients\":[\"bacon\", \"salad\"],\"tags\":[\"American\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/5/2_mealCreated2.txt
sleep 2
# Choisi son plat et envoit sa commande :
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restaurant_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}},{\"name\":\"Maxi mega burger of the death\",\"tags\":[\"American\"],\"restaurant\":{\"id\":$restaurant_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$gail_id\", \"lastName\":\"Oho\",\"firstName\":\"Gail\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restaurant_id,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/5/3_orderWithETA.txt
echo "Order made (temp/1/5_orderWithETA.txt):"
cat temp/5/3_orderWithETA.txt
echo "\nPress any key to continue..."
read
# Acception de l'ETA :
sed -i 's/WAITING/VALIDATED/g' temp/5/3_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/5/3_orderWithETA.txt | head -1)
# Envoi au système de la commande acceptée, le système poste un message dans le bus pour le restaurant :
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/5/3_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/5/4_validatedOrder.txt
echo "\nPress any key to continue..."
read
sleep 2
echo "As Jordan, I want to access to the order list, so that I can prepare the meal efficiently."
curl -X GET --silent "http://$restaurant/restaurants/$restaurant_id/orders/" > temp/5/5_pendingOrders.txt
echo "\nPending orders (temp/1/5_orderWithETA.txt):"
cat temp/5/5_pendingOrders.txt
echo "\nPress any key to continue..."
read