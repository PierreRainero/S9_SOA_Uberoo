package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewFeedback;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	private IUserDao userDao;
	
	@Autowired
	private IRestaurantDao restaurantDao;
	
	@Autowired
	private MessageProducer producer;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public List<MealDTO> findMealsByTag(String tag) {
		if(tag.isEmpty()) {
			return catalogDao.listMeals().stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
		}else {
			return catalogDao.findMealsByTag(tag).stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public List<MealDTO> findMealsByRestaurant(int restaurantId) throws UnknowRestaurantException {
		return catalogDao.findMealsByRestaurant(checkAndFindRestaurant(restaurantId)).stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
	}
	
	private Restaurant checkAndFindRestaurant(int id) throws UnknowRestaurantException {
		Optional<Restaurant> existingRestaurant = restaurantDao.findRestaurantById(id);
		
		if(!existingRestaurant.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+id);
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
		
		Meal meal = checkAndFindMeal(mealId);
		feedback.setMeal(meal);
		FeedbackDTO result = catalogDao.addFeedback(feedback).toDTO();
		
		producer.sendMessage(new NewFeedback(result, meal.toDTO()));
		
		return result;
	}

	private User checkAndFindUser(int id) throws UnknowUserException {
		Optional<User> userWrapped = userDao.findUserById(id);
		
		if(!userWrapped.isPresent()) {
			throw new UnknowUserException("Can't find user with id = "+id);
		}
		
		return userWrapped.get();
	}
	
	private Meal checkAndFindMeal(int id) throws UnknowMealException {
		Optional<Meal> mealWrapped = catalogDao.findMealById(id);
		
		if(!mealWrapped.isPresent()) {
			throw new UnknowMealException("Can't find meal with id = "+id);
		}
		
		return mealWrapped.get();
	}
}
