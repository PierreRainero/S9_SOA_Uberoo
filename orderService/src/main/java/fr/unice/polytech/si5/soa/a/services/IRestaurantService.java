package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;

/**
 * Class name	IRestaurantService
 * Date			20/10/2018
 * @author		PierreRainero
 *
 */
public interface IRestaurantService {
	/**
	 * Search restaurants in the catalog
	 * @param name name to search
	 * @return list of restaurants matching with the name
	 */
	public List<RestaurantDTO> findRestaurantByName(String name);
}
