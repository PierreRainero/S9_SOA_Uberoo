package fr.unice.polytech.si5.soa.a.services.component;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.NewRestaurant;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

@Primary
@Service("RestaurantService")
public class RestaurantServiceImpl implements IRestaurantService {
	@Autowired
	private IRestaurantDao restaurantDao;
	
	@Autowired
	private MessageProducer producer;

	@Override
	public RestaurantDTO findRestaurant(String name, String address) throws UnknowRestaurantException {
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurant(name, address);
		
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant : "+name);
		}
		
		return restaurantWrapped.get().toDTO();
	}

	@Override
	public RestaurantDTO addRestaurant(RestaurantDTO restaurant) {
		RestaurantDTO result = new Restaurant(restaurant).toDTO();
		NewRestaurant message = new NewRestaurant(result);
		producer.sendMessage(message);
		
		return result;
	}

	@Override
	public RestaurantDTO findRestaurantById(int id) throws UnknowRestaurantException {
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurantById(id);
		
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+id);
		}
		
		return restaurantWrapped.get().toDTO();
	}
}
