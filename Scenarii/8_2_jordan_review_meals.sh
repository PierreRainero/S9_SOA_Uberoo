
# -- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
echo "--- Creating context... ---"
# Creating Jordan
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oui\",\"firstName\":\"Jordan\"}" "http://$orderService/users" > temp/8/0_jordanId.txt
jordan_id=$(grep -Po '"id": *\K[^,]*' temp/8/0_jordanId.txt | head -1)
# Create a restaurant POST /restaurants
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/8/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/8/1_restaurantCreated.txt | head -1)
# Create a ramen meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/8/2_mealCreated.txt
meal_id=$(grep -Po '"id": *\K[^,]*' temp/8/2_mealCreated.txt | head -1)
meal=$(grep -Po '.+' temp/8/2_mealCreated.txt | head -1)
sleep 2
# ************** Scenario **************
echo "As Jordan, I want the customers to be able to review the meals so that I can improve them according to their feedback"
echo "Press any key to continue..."
read

# Choisi son plat et envoit sa commande :
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{ \"author\": \"Jordan\", \"content\": \"Super cool de ouf\", \"meal\": {\"id\": $meal_id,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]} }" "http://$restaurant/restaurants/$restaurant_id/meals/$meal_id/feedbacks" > temp/8/3_feedbackGiven.txt
cat temp/8/3_feedbackGiven.txt
echo "\nPress any key to continue..."
read
curl -X GET --silent "http://$restaurant/restaurants/$restaurant_id/meals/$meal_id/feedbacks" > temp/8/4_feedbacksForMealRamen.txt
cat temp/8/4_feedbacksForMealRamen.txt
echo "\nPress any key to continue..."
read

sleep 2
