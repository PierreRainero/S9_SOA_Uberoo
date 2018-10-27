package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;


/**
 * Class name	IMealService
 * Date			22/10/2018
 * @author		PierreRainero
 */
public interface IMealService {
	MealDTO addMeal(MealDTO meal, int restaurantId) throws UnknowRestaurantException;
    MealDTO findMealByName(String name) throws UnknowMealException;
}
