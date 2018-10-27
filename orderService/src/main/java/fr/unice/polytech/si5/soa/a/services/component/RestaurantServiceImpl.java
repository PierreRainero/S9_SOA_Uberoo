package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.NewRestaurant;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class name	RestaurantServiceImpl
 * @author 		PierreRainero
 * @see 		IRestaurantService
 * Date			20/10/2018
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
	public RestaurantDTO addRestaurant(NewRestaurant message) {
		//TODO: add new restaurant in db
		return new RestaurantDTO();
	}

}
