package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;

import java.util.List;

/**
 * Class name	IRestaurantService
 * Date			20/10/2018
 * @author		PierreRainero
 *
 * @version		1.2
 * Date			29/10/2018
 */
public interface IRestaurantService {
	/**
	 * Search restaurants in the catalog
	 * @param name name to search
	 * @return list of restaurants matching with the name
	 */
	List<RestaurantDTO> findRestaurantByName(String name);
	
	/**
	 * Add a {@link RestaurantDTO} to the system
	 * @param restaurantToAdd restaurant to add
	 * @return the restaurant added
	 */
	RestaurantDTO addRestaurant(RestaurantDTO restaurantToAdd);
	

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
	
	/**
	 * Add a {@link MealDTO} to the system
	 * @param mealToAdd meal to add
	 * @param restaurantName name of the restaurant that will receive the new meal
	 * @param restaurantAddress address of the restaurant that will receive the new meal
	 * @return the meal added
	 * @throws UnknowRestaurantException if the restaurant doesn't exist
	 */
	MealDTO addMeal(MealDTO mealToAdd, String restaurantName, String restaurantAddress) throws UnknowRestaurantException;
}
