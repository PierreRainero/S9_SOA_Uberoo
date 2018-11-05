# -- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
echo "--- Creating context... ---"
# Creating gail
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Oho\",\"firstName\":\"Gail\"}" "http://$orderService/users" > temp/2/0_gailId.txt
gail_id=$(grep -Po '"id": *\K[^,]*' temp/2/0_gailId.txt | head -1)
# Create a restaurant POST /restaurants
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > temp/2/1_restaurantCreated.txt
restaurant_id=$(grep -Po '"id": *\K[^,]*' temp/2/1_restaurantCreated.txt | head -1)
# Create a ramen meal POST /restaurants/{restaurantId}/meals
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/2/2_mealCreated1.txt
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Nem\",\"price\":7,\"ingredients\":[\"chicken\", \"egg\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > temp/2/2_mealCreated2.txt
sleep 2
# Create a coursier POST /coursiers
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Jamie\",\"accountNumber\":\"FR89 3704 0044 0532 0130 00\",\"latitude\":0.0, \"longitude\":0.0}" "http://$coursierservice/coursiers" > temp/2/3_coursierCreated.txt
sleep 2
# ************** Scenario **************
echo "As Gail, I can browse the food catalogue by categories so that I can immediately identify my favorite junk food"
echo "Press any key to continue..."
read
# Récupère tous les plats qui ont le tag 'Asian' :
curl -X GET --silent "http://$orderService/meals?tags=Asian" > temp/2/3_resultOfMealsResearch.txt
echo "Result (temp/2/3_resultOfMealsResearch.txt):"
cat temp/2/3_resultOfMealsResearch.txt
echo "Press any key to continue..."
read