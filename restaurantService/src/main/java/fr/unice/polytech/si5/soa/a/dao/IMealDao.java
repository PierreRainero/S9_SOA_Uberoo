package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;

import java.util.Optional;

/**
 * Class name	IMealDao
 * Date			22/10/2018
 * @author		PierreRainero
 */
public interface IMealDao {
	Meal addMeal(Meal meal);
    Optional<Meal> findMealByName(String name);
    Optional<Meal> findMealById(int id);
    
    Ingredient addIngredient(Ingredient ingredient);
    Optional<Ingredient> findIngredientByName(String name);
}
