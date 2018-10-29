package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class name	RestaurantServiceImpl
 * @author 		PierreRainero
 * @see 		IRestaurantService
 * Date			20/10/2018
 * 
 * @version		1.2
 * Date			29/10/2018
 **/
@Primary
@Service("RestaurantService")
public class RestaurantServiceImpl implements IRestaurantService {
	@Autowired
	private IRestaurantDao restaurantDao;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public List<RestaurantDTO> findRestaurantByName(String name) {
		if(name.isEmpty()) {
			return restaurantDao.listRestaurants().stream().map(restaurant -> restaurant.toDTO()).collect(Collectors.toList());
		}else {
			return restaurantDao.findRestaurantByName(name).stream().map(restaurant -> restaurant.toDTO()).collect(Collectors.toList());
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public MealDTO addMeal(MealDTO mealToAdd, String restaurantName, String restaurantAddress) throws UnknowRestaurantException {
		Meal meal = new Meal(mealToAdd);
		meal.setRestaurant(checkAndFindRestaurant(restaurantName, restaurantAddress));
		
		return restaurantDao.addMeal(meal).toDTO();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public RestaurantDTO addRestaurant(RestaurantDTO restaurantToAdd) {
		return restaurantDao.addRestaurant(new Restaurant(restaurantToAdd)).toDTO();
	}
	
	private Restaurant checkAndFindRestaurant(String name, String address) throws UnknowRestaurantException {
		Optional<Restaurant> existingRestaurant = restaurantDao.findRestaurantByNameAndAddress(name, address);
		
		if(!existingRestaurant.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with name = "+name);
		}
		
		return existingRestaurant.get();
	}
}
