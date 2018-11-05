package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

/**
 * Class name	RestaurantController
 * Date			23/10/2018
 * @author		PierreRainero
 *
 * @version		1.2
 * Date			28/10/2018
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	private static Logger logger = LogManager.getLogger(RestaurantController.class);
	private final static String HEADERS = "application/JSON; charset=UTF-8";
	
	@Autowired
	private IRestaurantService restaurantService;
	
	@Autowired
	private IMealService mealService;
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> addRestaurant(@RequestBody RestaurantDTO restaurant) {
		return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
	}
	
	@RequestMapping(value = "/{restaurantId}/meals",
			method = RequestMethod.POST,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> addMeal(@PathVariable("restaurantId") String restaurantId, @RequestBody MealDTO meal) {
		int convertedId = Integer.parseInt(restaurantId);
		
		try {
			return ResponseEntity.ok(mealService.addMeal(meal, convertedId));
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/{restaurantId}/meals/{mealId}/feedbacks",
			method = RequestMethod.GET,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> getFeedbacks(
			@PathVariable("restaurantId") String restaurantId,
			@PathVariable("mealId") String mealId) {
		int mealIdAsInt = Integer.parseInt(mealId);
		
		try {
			return ResponseEntity.ok(mealService.findFeedbackForMeal(mealIdAsInt));
		} catch (UnknowMealException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

    @RequestMapping(value = "/{restaurantId}/meals/{mealId}/feedbacks",
            method = RequestMethod.POST,
            consumes = {HEADERS},
            produces = {HEADERS})
    public ResponseEntity<?> addFeedback(@PathVariable("restaurantId") String restaurantId,
                                         @PathVariable("mealId") String mealId, @RequestBody FeedbackDTO feedback) {
        int convertedId = Integer.parseInt(restaurantId);

        try {
            return ResponseEntity.ok(mealService.addFeedback(feedback, Integer.parseInt(mealId)));
        } catch (UnknowMealException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

	@RequestMapping(value = "/",
			method = RequestMethod.GET,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> getAllRestaurants() {
		return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}

}
