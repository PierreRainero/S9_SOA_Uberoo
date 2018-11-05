package fr.unice.polytech.si5.soa.a.services.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

@Primary
@Service("RestaurantService")
public class RestaurantServiceImpl implements IRestaurantService {
	@Autowired
	private IRestaurantDao restaurantDao;
	
	@Override
	public RestaurantDTO addRestaurant(RestaurantDTO restaurantToAdd) {
		return restaurantDao.addRestaurant(new Restaurant(restaurantToAdd)).toDto();
	}

	@Override
	public RestaurantDTO findRestaurant(String name, String address) {
		// TODO Auto-generated method stub
		return null;
	}

}
