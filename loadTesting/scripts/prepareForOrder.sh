# -- Hostnames ---
orderService="localhost:9555"
restaurant="localhost:9777"
coursierservice="localhost:9888"
# -- Context ---
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"lastName\":\"Lenon\",\"firstName\":\"Bob\"}" "http://$orderService/users" > ../user-files/bodies/0_bob.json
bob_id=$(grep -Po '"id": *\K[^,]*' ../user-files/bodies/0_bob.json | head -1)
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Asiakeo\",\"restaurantAddress\":\"407 ch. de l'oued\",\"meals\":[]}" "http://$restaurant/restaurants" > ../user-files/bodies/1_asiakeo.json
restaurant_id=$(grep -Po '"id": *\K[^,]*' ../user-files/bodies/1_asiakeo.json | head -1)
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Ramen\",\"price\":3,\"ingredients\":[\"pork\"],\"tags\":[\"Asian\"]}" "http://$restaurant/restaurants/$restaurant_id/meals" > ../user-files/bodies/Useless_meal.json
curl -X POST --silent -H "Content-Type:application/JSON; charset=UTF-8" -d "{\"id\":-1,\"name\":\"Jamie\",\"accountNumber\":\"FR89 3704 0044 0532 0130 00\",\"latitude\":0.0, \"longitude\":0.0}" "http://$coursierservice/coursiers" > ../user-files/bodies/Useless_coursier.json
curl -X GET --silent "http://$orderService/meals?tag=Asian" > ../user-files/bodies/2_asian_meals.json
restaurant_id_for_orderService=$(grep -Po '"id": *\K[^,]*' ../user-files/bodies/2_asian_meals.json | head -1)
echo "{ \"id\": -1, \"meals\": [ {\"name\":\"Ramen\",\"tags\":[\"Asian\"],\"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"}} ], \"transmitter\": { \"id\": \"$bob_id\", \"firstName\": \"Bob\", \"lastName\": \"\" }, \"deliveryAddress\": \"930 Route des Colles, 06410 Biot\", \"eta\": null, \"state\": \"WAITING\", \"restaurant\":{\"id\":$restaurant_id_for_orderService,\"name\":\"Asiakeo\",\"restaurantAddress\":\"690 Route de Grasse, 06600 Antibes\"} }" > ../user-files/bodies/3_order.json