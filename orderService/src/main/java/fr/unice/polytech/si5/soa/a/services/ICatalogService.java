package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;

import java.util.List;

/**
 * Class name	ICatalogService
 * Date			01/10/2018
 * @author		PierreRainero
 *
 * @version		1.1
 * Date			21/10/2018
 */
public interface ICatalogService {
	/**
	 * Search meals in the catalog using a tag
	 * @param tag tag to search
	 * @return list of meals matching with the tag
	 */
	List<MealDTO> findMealsByTag(String tag);
	
	/**
	 * Search all meals of a restaurants in the catalog
	 * @param restaurantId id of the restaurant to search
	 * @return list of restaurant's meals
	 * @throws UnknowRestaurantException if the restaurant doesn't exist
	 */
	List<MealDTO> findMealsByRestaurant(int restaurantId) throws UnknowRestaurantException;
}
