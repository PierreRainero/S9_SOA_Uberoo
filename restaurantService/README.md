# Restaurant Service v1.2

## Description

Assure la gestion des commandes à préparer pour les restaurants.

## Technologies

Java 1.8 (JEE) avec les frameworks Spring et Hibernate.

## API exposée (HTTP REST)

`/restaurants/{restaurantId}/orders` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de trouver toutes les commandes à préparer  
`/restaurants/{restaurantId}/orders/{orderId}/` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- PUT (content-type : JSON, encoding : UTF-8), body : RestaurantOrderDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de marquer une commande comme terminée ou non  
`/restaurants` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- POST (content-type : JSON, encoding : UTF-8), body : RestaurantDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet d'ajouter un nouveau restaurant  
`/restaurants/{restaurantId}/meals` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- POST (content-type : JSON, encoding : UTF-8), body : MealDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet d'ajouter un nouveau plat  
`/restaurants/{restaurantId}/meals/{mealId}/feedbacks` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- POST (content-type : JSON, encoding : UTF-8), body : FeedbackDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet d'ajouter un avis à un plat  
`/restaurants/{restaurantId}/meals/{mealId}/feedbacks` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de lister tous les avis d'un plat  

## Messages consommés du bus  

**NEW_ORDER** :  
`{"address":"410 ch de chez moi","food":["plat1","plat2"],"type":"NEW_ORDER","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant"}`

## Messages transmis au bus

**NEW_RESTAURANT** :  
`{"type":"NEW_RESTAURANT","name":"Mon restaurant","address":"25 rue du restaurant"}`

**NEW_MEAL** :  
`{"type":"NEW_MEAL","name":"Mon plat","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant"}`