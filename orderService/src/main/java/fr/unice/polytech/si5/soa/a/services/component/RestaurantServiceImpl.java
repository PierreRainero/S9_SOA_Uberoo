package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
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
	
	@Autowired
	private IUserDao userDao;

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

	@Override
	/**
	 * {@inheritDoc}
	 */
	public FeedbackDTO addFeedback(FeedbackDTO feedbackToAdd, int authorId, int mealId) throws UnknowMealException, UnknowUserException {
		Feedback feedback = new Feedback(feedbackToAdd);
		feedback.setAuthor(checkAndFindUser(authorId));
		feedback.setMeal(checkAndFindMeal(mealId));
		
		return restaurantDao.addFeedback(feedback).toDTO();
	}

	private User checkAndFindUser(int id) throws UnknowUserException {
		Optional<User> userWrapped = userDao.findUserById(id);
		
		if(!userWrapped.isPresent()) {
			throw new UnknowUserException("Can't find user with id = "+id);
		}
		
		return userWrapped.get();
	}
	
	private Meal checkAndFindMeal(int id) throws UnknowMealException {
		Optional<Meal> mealWrapped = restaurantDao.findMealById(id);
		
		if(!mealWrapped.isPresent()) {
			throw new UnknowMealException("Can't find meal with id = "+id);
		}
		
		return mealWrapped.get();
	}
}
