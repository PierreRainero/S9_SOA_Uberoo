package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	IRestaurantDao
 * Date			22/10/2018
 * @author		PierreRainero
 */
public interface IRestaurantDao {
	Restaurant addRestaurant(Restaurant restaurant);
	Optional<Restaurant> findRestaurant(String name, String address);
	Optional<Restaurant> findRestaurantById(int id);
	List<Restaurant> getAllRestaurants();
}
