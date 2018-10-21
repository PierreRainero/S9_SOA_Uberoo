package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	IRestaurantDao
 * Date			20/10/2018
 * @author 		PierreRainero
 *
 */
public interface IRestaurantDao {
	/**
	 * Add an {@link Restaurant} into the database
	 * @param restaurantToAdd restaurant to add
	 * @return the saved restaurant
	 */
	Restaurant addRestaurant(Restaurant restaurantToAdd);
	
	/**
	 * Search a restaurant in the database using his id
	 * @param id id to use
	 * @return the restaurant wrapped in an {@link Optional} if the restaurant exists, Optional.empty() otherwise
	 */
	Optional<Restaurant> findRestaurantById(int id);
	
	/**
	 * Search restaurants in the database using their names
	 * @param name name to used
	 * @return list of restaurants matching with the name
	 */
	List<Restaurant> findRestaurantByName(String name);
}
