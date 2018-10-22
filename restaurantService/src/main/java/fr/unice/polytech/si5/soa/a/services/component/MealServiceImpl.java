package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.IngredientDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.NewMeal;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service("MealService")
public class MealServiceImpl implements IMealService {
    @Autowired
    private IMealDao mealDao;

    @Autowired
	private IRestaurantDao restaurantDao;
    
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
	public MealDTO addMeal(MealDTO meal, RestaurantDTO restaurant) throws UnknowRestaurantException {
		Meal mealToInsert = new Meal(meal);
		
		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurantById(restaurant.getId());
		if(!restaurantWrapped.isPresent()) {
			throw new UnknowRestaurantException("Can't find restaurant with id = "+restaurant.getId());
		}
		mealToInsert.setRestaurant(restaurantWrapped.get());
		
		for(IngredientDTO tmp : meal.getIngredients()) {
			mealToInsert.addIngredient(findOrCreateIngredient(tmp));
		}
		
		MealDTO result = mealDao.addMeal(mealToInsert).toDTO();
		NewMeal message = new NewMeal(result, restaurantWrapped.get().getName(), restaurantWrapped.get().getRestaurantAddress());
		producer.sendMessage(message);
		
		return result;
	}
	
	private Ingredient findOrCreateIngredient(IngredientDTO ingredientDTO) {
		Optional<Ingredient> ingredient = mealDao.findIngredientByName(ingredientDTO.getName());
		
		if(!ingredient.isPresent()) {
			return mealDao.addIngredient(new Ingredient(ingredientDTO));
		}
		
		return ingredient.get();
	}
}
