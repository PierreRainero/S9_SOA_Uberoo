package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;

public interface IRestaurantDao {
	Restaurant addRestaurant(Restaurant restaurant);
	Optional<Restaurant> findRestaurant(String name, String address);
	Optional<Restaurant> findRestaurantById(int id);
}
