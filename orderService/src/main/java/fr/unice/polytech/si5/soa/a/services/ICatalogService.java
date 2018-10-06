package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;

/**
 * Class name	ICatalogService
 * Date			01/10/2018
 * @author		PierreRainero
 *
 */
public interface ICatalogService {
	/**
	 * Search meals in the catalog using a tag
	 * @param tag tag to search
	 * @return list of meals matching with the tag
	 */
	List<MealDTO> findMealsByTag(String tag);
}
