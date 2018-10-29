package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	IRestaurantDao
 * Date			20/10/2018
 * @author 		PierreRainero
 *
 */
public interface IRestaurantDao {
	/**
	 * Add a {@link Restaurant} into the database
	 * @param restaurantToAdd restaurant to add
	 * @return the saved restaurant
	 */
	Restaurant addRestaurant(Restaurant restaurantToAdd);
	
	/**
	 * Add a {@link Meal} into the database
	 * @param mealToAdd meal to add
	 * @return the saved meal
	 */
	Meal addMeal(Meal mealToAdd);
	
	/**
	 * Search a restaurant in the database using his id
	 * @param id id to use
	 * @return the restaurant wrapped in an {@link Optional} if the restaurant exists, Optional.empty() otherwise
	 */
	Optional<Restaurant> findRestaurantById(int id);
	
	/**
	 * Search a restaurant using his name and his address
	 * @param name name of the searched restaurant
	 * @param address address of the searched restaurant
	 * @return the restaurant wrapped in an {@link Optional} if the restaurant exists, Optional.empty() otherwise
	 */
	Optional<Restaurant> findRestaurantByNameAndAddress(String name, String address);
	
	/**
	 * Search restaurants in the database using their names
	 * @param name name to used
	 * @return list of restaurants matching with the name
	 */
	List<Restaurant> findRestaurantByName(String name);
	
	/**
	 * List every existing restaurants
	 * @return list of restaurants
	 */
	List<Restaurant> listRestaurants();
}
