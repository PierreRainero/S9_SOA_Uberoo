package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	ICatalogDao
 * Date			01/10/2018
 * @author		PierreRainero
 * 
 * @version		1.1
 * Date			29/10/2018
 */
public interface ICatalogDao {
	/**
	 * Search a meal in the database using his name
	 * @param name name to search
	 * @return the meal wrapped in an {@link Optional} if the meal exists, Optional.empty() otherwise
	 */
	Optional<Meal> findMealByName(String name);
	
	/**
	 * List every existing meals
	 * @return list of meals
	 */
	List<Meal> listMeals();
	
	/**
	 * Search meals in the database using a tag
	 * @param tag tag to search
	 * @return list of meals matching with the tag
	 */
	List<Meal> findMealsByTag(String tag);
	
	/**
	 * Search all meals of a restaurants in the database
	 * @param restaurant restaurant to search
	 * @return list of restaurant's meals
	 */
	List<Meal> findMealsByRestaurant(Restaurant restaurant);
	
	/**
	 * Add a {@link Feedback} into the database
	 * @param feedbackToAdd feedback to add
	 * @return the saved feedback
	 */
	Feedback addFeedback(Feedback feedbackToAdd);
	
	/**
	 * Search a meal in the database using his id
	 * @param id id to use
	 * @return the restaurant wrapped in an {@link Optional} if the meal exists, Optional.empty() otherwise
	 */
	Optional<Meal> findMealById(int id);
}
