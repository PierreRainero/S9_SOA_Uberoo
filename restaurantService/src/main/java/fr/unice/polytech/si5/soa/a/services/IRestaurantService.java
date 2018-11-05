package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;

import java.util.List;

/**
 * Class name	IRestaurantService
 * Date			22/10/2018
 * @author		PierreRainero
 */
public interface IRestaurantService {
	RestaurantDTO addRestaurant(RestaurantDTO restaurant);
	RestaurantDTO findRestaurant(String name, String address) throws UnknowRestaurantException;
	RestaurantDTO findRestaurantById(int id) throws UnknowRestaurantException;
	List<RestaurantDTO> getAllRestaurants();
}
