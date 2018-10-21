# Order Service v1.1

## Description

Assure la prise de commande et la recherche dans le catalogue.

## Technologies

Java 1.8 (JEE) avec les frameworks Spring et Hibernate.

## API exposée (HTTP REST)

`/orders` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- POST (content-type : JSON, encoding : UTF-8), body : OrderDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet d'ajouter une nouvelle commande  
`/orders/{orderId}/` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- PUT (content-type : JSON, encoding : UTF-8), body : OrderDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de valider ou non une commande en changeant son status ("state")  
`/meals` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8), queryParam : tag=AFoodTag  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de trouver tous les plats correspondant à un tag ("Asian" par exemple)  
`/restaurants` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8), queryParam : name=ARestaurantName  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de trouver tous les restaurants dont le nom contient la chaine de caractères passée en paramètre  
`/restaurants/{restaurantId}/meals` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de trouver tous les plats d'un restaurant (dont l'identifiant est dans l'URI)

## Messages transmis au bus

**NEW_ORDER** :  
`{"address":"410 ch de chez moi","food":["plat1","plat2"],"type":"NEW_ORDER","restaurantName":"Mon restaurant","restaurantAddress":"25 rue du restaurant"}`

**PROCESS_PAYMENT** :  
`{"type":"PROCESS_PAYMENT","account":"numero de compte","amont":10.0}`