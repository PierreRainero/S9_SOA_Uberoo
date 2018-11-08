# OK-- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
echo "--- Creating context... ---"
# Creating Erin
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Niaise\",\"firstName\":\"Erin\"}" "http://$orderService/users" > temp/4/0_erinId.txt
erin_id=$(grep -Po '"id": *\K[^,]*' temp/4/0_erinId.txt | head -1)
# Create a restaurant POST /restaurants
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/4/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/4/1_restaurantCreated.txt | head -1)
# Create a ramen meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/4/2_mealCreated1.txt
sleep 2
# Create a coursier POST /coursiers
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Jamie\",\"accountNumber\":\"FR89 3704 0044 0532 0130 00\",\"latitude\":0.0, \"longitude\":0.0}" "http://$coursierservice/coursiers" > temp/4/3_coursierCreated.txt
sleep 2
# ************** Scenario **************
echo "As Erin, I can pay directly by credit card on the platform, so that I only have to retrieve my food when delivered"
echo "Press any key to continue..."
read
# Récupère les plats du catalogue de la catégorie "Asian" :
curl -X GET --silent "http://$orderService/meals" > temp/4/4_resultOfCatalog.txt
restaurant_id_for_orderService=$(grep -Po '"id": *\K[^,]*' temp/4/4_resultOfCatalog.txt | head -1)
echo "Result (temp/4/4_resultOfCatalog.txt):"
cat temp/4/4_resultOfCatalog.txt
echo "Press any key to continue..."
read
# Envoi de la commande au système et récupération de l'ETA
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$erin_id\", \"firstName\": \"Bob\", \"lastName\": \"\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" "http://$orderService/orders" > temp/4/5_orderWithETA.txt
echo "Result (temp/4/5_orderWithETA.txt):"
cat temp/4/5_orderWithETA.txt
echo "\nPress any key to continue..."
read
# Acception de l'ETA :
sed -i 's/WAITING/VALIDATED/g' temp/4/5_orderWithETA.txt
order_id=$(grep -Po '"id": *\K[^,]*' temp/4/5_orderWithETA.txt | head -1)
# Envoi au système, le système poste un message dans le bus pour le restaurant :
curl -X PUT --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "$(tail -1 temp/4/5_orderWithETA.txt)" "http://$orderService/orders/$order_id" > temp/4/6_validatedOrder.txt
echo "Press any key to continue..."
read
sleep 2
# Envoie du paiement de la commande :
orderAmount=$(grep -Po '"price": *\K[^,]*' temp/4/6_validatedOrder.txt | tail -1)
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"account\":\"FR89 3704 0044 0532 0130 00\",\"amount\":$orderAmount, \"state\":\"SENT\"}" "http://$orderService/orders/$order_id/payments" > temp/4/7_paymentCreated.txt