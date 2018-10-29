package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IOrderService;

/**
 * Class name	OrderServiceImpl
 * @see			IOrderService
 * Date			08/10/2018
 * @author		PierreRainero
 */
@Primary
@Service("OrderService")
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IRestaurantDao restaurantDao;
	
	@Autowired
	private IMealDao mealDao;
	
	@Override
	public RestaurantOrderDTO addOrder(RestaurantOrderDTO orderToAdd) throws UnknowRestaurantException, UnknowMealException {
		RestaurantOrder order = new RestaurantOrder(orderToAdd);
		
		Restaurant restaurant = getRestaurantById(orderToAdd.getRestaurant().getId());
		order.setRestaurant(restaurant);
		
		for(MealDTO tmp : orderToAdd.getMeals()) {
			Optional<Meal> mealRetrieved = mealDao.findMealById(tmp.getId());
			
			if(!mealRetrieved.isPresent()) {
				throw new UnknowMealException("Can't find meal with id = "+tmp.getId());
			}
			order.addMeal(mealRetrieved.get());
		}
		
		return orderDao.addOrder(order).toDTO();
	}

	@Override
	public RestaurantOrderDTO updateOrder(RestaurantOrderDTO orderToUpdate) throws UnknowOrderException {
		Optional<RestaurantOrder> orderWrapped = orderDao.findOrderById(orderToUpdate.getId());
		if(!orderWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = "+orderToUpdate.getId());
		}
		RestaurantOrder order = orderWrapped.get();
		order.setState(orderToUpdate.getState());
		
		return orderDao.updateOrder(order).toDTO();
	}

	@Override
	public List<RestaurantOrderDTO> getOrdersToDo(int restaurantId) throws UnknowRestaurantException {
		Restaurant restaurantRetreived = getRestaurantById(restaurantId);
		
		return orderDao.getOrdersToDo(restaurantRetreived).stream().map(order -> order.toDTO()).collect(Collectors.toList());
	}
	
	private Restaurant getRestaurantById(int id) throws UnknowRestaurantException {
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurantById(id);
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+id);
		}
		
		return restaurantWrapped.get();
	}

	@Override
	public RestaurantOrderDTO addOrder(List<String> meals, String restaurantName, String restaurantAddress) throws UnknowRestaurantException, UnknowMealException {
		RestaurantOrder order = new RestaurantOrder();
		Restaurant restaurant = checkAndFindRestaurant(restaurantName, restaurantAddress);
		order.setRestaurant(restaurant);
		createMealsList(order, meals, restaurant);
		
		return orderDao.addOrder(order).toDTO();
	}
	
	private Restaurant checkAndFindRestaurant(String name, String address) throws UnknowRestaurantException {
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurant(name, address);
		
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant : "+name);
		}
		
		return restaurantWrapped.get();
	}
	
	private void createMealsList(RestaurantOrder order, List<String> meals, Restaurant restaurant) throws UnknowMealException {
		for (String foodName : meals) {
			Optional<Meal> mealWrapped = mealDao.findMealByNameForRestaurant(foodName, restaurant);
			
			if(!mealWrapped.isPresent()) {
				throw new UnknowMealException("Can't find meal with name = "+foodName);
			}
			order.addMeal(mealWrapped.get());
		}
	}

}
