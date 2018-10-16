# Coursier Service

## Description
Assure la prise en charge des livraisons des commandes.

## Technologies
Java 1.8 (JEE) avec les frameworks Spring et Hibernate.

## API exposée (HTTP REST)
`/deliveries` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- POST (content-type : JSON, encoding : UTF-8), body : _**NewOrder** (messageBus)_  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet d'ajouter une nouvelle livraison à effectuer   
`/deliveries/{deliveryId}/` :   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- PUT (content-type : JSON, encoding : UTF-8), body : DeliveryDTO  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de valider ou non une livraison en changeant son status ("state")   
`/deliveries` :  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- GET (content-type : JSON, encoding : UTF-8), queryParam : tag=AFoodTag  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Permet de trouver toutes les livraisons à faire   