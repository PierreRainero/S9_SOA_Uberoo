package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;

import java.util.Optional;

public interface IRestaurantDao {
    Optional<Restaurant> findRestaurantById(Integer idRestaurant);
}
