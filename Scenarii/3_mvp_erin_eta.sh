# -- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
echo "Scenario#3: As Erin, I want to know before ordering the estimated time of delivery of the meal so that I can schedule my work around it, and be ready when it arrives."
echo ""
echo "--- Creating context... ---"
# Creating Erin
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Niaise\",\"firstName\":\"Erin\"}" "http://$orderService/users" > temp/3/0_erinId.txt
erin_id=$(grep -Po '"id": *\K[^,]*' temp/3/0_erinId.txt | head -1)
# Create a restaurant POST /restaurants
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/3/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/3/1_restaurantCreated.txt | head -1)
# Create a ramen meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/3/2_mealCreated1.txt
sleep 2
# Create a coursier POST /coursiers
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Jamie\",\"accountNumber\":\"FR89 3704 0044 0532 0130 00\",\"latitude\":0.0, \"longitude\":0.0}" "http://$coursierservice/coursiers" > temp/3/3_coursierCreated.txt
sleep 2
echo "--- Context created ---"
echo ""
# ************** Scenario **************
echo "*******1- As Erin, I search the list of meals for all restaurant"
echo "Press any key to continue..."
read
# Récupère les plats du catalogue :
curl -X GET --silent "http://$orderService/meals" > temp/3/4_resultOfCatalog.txt
restaurant_id_for_orderService=$(grep -Po '"id": *\K[^,]*' temp/3/4_resultOfCatalog.txt | head -1)
echo "Result (temp/3/4_resultOfCatalog.txt):"
cat temp/3/4_resultOfCatalog.txt
echo ""
echo ""
echo "Press any key to continue..."
read

# Envoi de la commande au système et récupération de l'ETA
echo "*******2- As Erin, I choose my meal and send it to the system"
echo "Press any key to continue..."
read
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$erin_id\", \"firstName\": \"Bob\", \"lastName\": \"\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/3/5_orderWithETA.txt
echo "*******The system show me the ETA"
echo "Result (temp/3/5_orderWithETA.txt):"
cat temp/3/5_orderWithETA.txt
echo ""
echo ""
echo "Press any key to continue..."
read

# Acception de l'ETA :
echo "*******3- As Erin, I decide to accept the ETA"
echo "Press any key to continue..."
read
sed -i 's/WAITING/VALIDATED/g' temp/3/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/3/5_orderWithETA.txt | head -1)
# Envoi au système, le système poste un message dans le bus pour le restaurant :
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/3/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/3/6_validatedOrder.txt
echo ""
echo ""
echo "Press any key to continue..."
read