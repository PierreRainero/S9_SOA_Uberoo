package fr.unice.polytech.si5.soa.a.dao;

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
}
