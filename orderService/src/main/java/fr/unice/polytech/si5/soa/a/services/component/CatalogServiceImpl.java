package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;

/**
 * Class name	CatalogServiceImpl
 * @see 		ICatalogService
 * Date			01/10/2018
 * @author		PierreRainero
 * 
 * @version		1.1
 * Date			21/10/2018
**/
@Primary
@Service("CatalogService")
public class CatalogServiceImpl implements ICatalogService {
	@Autowired
	private ICatalogDao catalogDao;
	
	@Autowired
	private IRestaurantDao restaurantDao;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public List<MealDTO> findMealsByTag(String tag) {
		return catalogDao.findMealsByTag(tag).stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public List<MealDTO> findMealsByRestaurant(int restaurantId) throws UnknowRestaurantException {
		Optional<Restaurant> existingRestaurant = restaurantDao.findRestaurantById(restaurantId);
		
		if(!existingRestaurant.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+restaurantId);
		}
		
		return catalogDao.findMealsByRestaurant(existingRestaurant.get()).stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
	}
}
