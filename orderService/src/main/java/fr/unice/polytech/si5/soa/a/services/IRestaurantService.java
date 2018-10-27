package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.NewRestaurant;

import java.util.List;

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

	RestaurantDTO addRestaurant(NewRestaurant message);
}
