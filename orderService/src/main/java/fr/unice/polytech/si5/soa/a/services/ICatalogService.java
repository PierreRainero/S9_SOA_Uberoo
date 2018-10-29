package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;

import java.util.List;

/**
 * Class name	ICatalogService
 * Date			01/10/2018
 * @author		PierreRainero
 *
 * @version		1.2
 * Date			29/10/2018
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
	
	/**
	 * 
	 * Add a {@link FeedbackDTO} to the system
	 * @param feedbackToAdd feedback to add
	 * @param authorId id of the author
	 * @param mealId id of the meal
	 * @return the feedback added
	 * @throws UnknowMealException if the meal doesn't exist
	 * @throws UnknowUserException if the author doesn't exist
	 */
	FeedbackDTO addFeedback(FeedbackDTO feedbackToAdd, int authorId, int mealId) throws UnknowMealException, UnknowUserException;
}
