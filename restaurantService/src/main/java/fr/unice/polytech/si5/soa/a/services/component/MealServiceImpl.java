package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.IngredientDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.NewMeal;
import fr.unice.polytech.si5.soa.a.dao.IFeedbackDao;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class name	MealServiceImpl
 * @see			IMealService
 * Date			22/10/2018
 * @author		PierreRainero
 * 
 * @version		1.2
 * Date			28/10/2018
 */
@Primary
@Service("MealService")
public class MealServiceImpl implements IMealService {
    @Autowired
    private IMealDao mealDao;

    @Autowired
	private IRestaurantDao restaurantDao;
    
    @Autowired
    private IFeedbackDao feedbackDao;
    
    @Autowired
	private MessageProducer producer;
    
    @Override
    public MealDTO findMealByName(String name) throws UnknowMealException {
        Optional<Meal> resultWrapped = mealDao.findMealByName(name);

        if(!resultWrapped.isPresent()){
            throw new UnknowMealException("Can't find meal with name = "+name);
        }

        return resultWrapped.get().toDTO();
    }


	@Override
	public MealDTO addMeal(MealDTO meal, int restaurantId) throws UnknowRestaurantException {
		Meal mealToInsert = new Meal(meal);
		
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurantById(restaurantId);
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+restaurantId);
		}
		mealToInsert.setRestaurant(restaurantWrapped.get());
		
		for(IngredientDTO tmp : meal.getIngredients()) {
			mealToInsert.addIngredient(findOrCreateIngredient(tmp));
		}
		
		MealDTO result = mealDao.addMeal(mealToInsert).toDTO();
		NewMeal message = new NewMeal(result, restaurantWrapped.get().getName(), restaurantWrapped.get().getRestaurantAddress(), result.getPrice());
		producer.sendMessage(message);
		
		return result;
	}
	
	@Override
	public FeedbackDTO addFeedback(FeedbackDTO feedbackToAdd, int mealID) throws UnknowMealException {
		 Feedback feedback = new Feedback(feedbackToAdd);
		 feedback.setMeal(checkAndFindMeal(mealID));
		 
		 return feedbackDao.addFeedback(feedback).toDTO();
	}


	@Override
	public List<FeedbackDTO> findFeedbackForMeal(int mealId) throws UnknowMealException {
		 return feedbackDao.findFeedBackByMeal(checkAndFindMeal(mealId)).stream().map(feedback -> feedback.toDTO()).collect(Collectors.toList());
	}
	
	private Meal checkAndFindMeal(int id) throws UnknowMealException {
		Optional<Meal> resultWrapped = mealDao.findMealById(id);
		 if(!resultWrapped.isPresent()){
	            throw new UnknowMealException("Can't find meal with id = "+id);
	     }
		 
		 return resultWrapped.get();
	}
	
	private Ingredient findOrCreateIngredient(IngredientDTO ingredientDTO) {
		Optional<Ingredient> ingredient = mealDao.findIngredientByName(ingredientDTO.getName());
		
		if(!ingredient.isPresent()) {
			return mealDao.addIngredient(new Ingredient(ingredientDTO));
		}
		
		return ingredient.get();
	}
}
