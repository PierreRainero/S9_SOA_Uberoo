# S9_SOA_Uberoo

## User Stories

- As Gail or Erin, I can order my lunch from a restaurant so that the food is delivered to my place;
- As Gail, I can browse the food catalogue by categories so that I can immediately identify my favorite junk food;
- As Erin, I want to know before ordering the estimated time of delivery of the meal so that I can schedule my work around it, and be ready when it arrives.
- As Erin, I can pay directly by credit card on the platform, so that I only have to retrieve my food when delivered;
- As Jordan, I want to access to the order list, so that I can prepare the meal efficiently.
- As Jamie, I want to know the orders that will have to be delivered around me, so that I can choose one and go to the restaurant to begin the course.
- As Jamie, I want to notify that the order has been delivered, so that my account can be credited and the restaurant can be informed.

- As Jordan, I want the customers to be able to review the meals so that I can improve them according to their feedback;
- As a customer (Gail, Erin), I want to track the geolocation of the coursier in real time, so that I can anticipate when I will eat.
- As Terry, I want to get some statistics (speed, cost) about global delivery time and delivery per coursier.

## Liste des messages

**NEW_ORDER** :  
`{"address":"410 ch de chez moi","food":["plat1","plat2"],"type":"NEW_ORDER","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant"}`

**PROCESS_PAYMENT** :  
`{"type":"PROCESS_PAYMENT","account":"numero de compte","amount":10.0,"id":-1}`

**PAYMENT_CONFIRMATION** :  
`{"type":"PAYMENT_CONFIRMATION","status":true,"id":-1}`  

**NEW_RESTAURANT** :  
`{"type":"NEW_RESTAURANT","name":"Mon restaurant","address":"25 rue du restaurant"}`

**NEW_MEAL** :  
`{"type":"NEW_MEAL","name":"Mon plat","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant","price":10.5,"tags":["Mon tag"]}`  

**NEW_FEEDBACK** :  
`{"type":"NEW_FEEDBACK","author":"prenom nom","content":"mon avis","mealName":"Mon plat","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant"}`