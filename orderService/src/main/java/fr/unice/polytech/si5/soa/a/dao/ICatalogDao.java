package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Meal;

/**
 * Class name	ICatalogDao
 * Date			01/10/2018
 * @author		PierreRainero
 *
 */
public interface ICatalogDao {
	/**
	 * Search a meal in the database using his name
	 * @param name name to search
	 * @return the meal wrapped in an {@link Optional} if the meal exists, Optional.empty() otherwise
	 */
	Optional<Meal> findMealByName(String name);
}
