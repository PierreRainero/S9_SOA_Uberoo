package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;

import java.util.List;

/**
 * Class name	IMealService
 * Date			22/10/2018
 * @author		PierreRainero
 * 
 * @version		1.2
 * Date			28/10/2018
 */
public interface IMealService {
	MealDTO addMeal(MealDTO meal, int restaurantId) throws UnknowRestaurantException;
    MealDTO findMealByName(String name) throws UnknowMealException;
    
    FeedbackDTO addFeedback(FeedbackDTO feedbackToAdd, int mealID) throws UnknowMealException;
    List<FeedbackDTO> findFeedbackForMeal(int mealId) throws UnknowMealException;
}
