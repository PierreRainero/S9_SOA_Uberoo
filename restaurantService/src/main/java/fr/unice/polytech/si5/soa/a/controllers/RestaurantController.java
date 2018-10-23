package fr.unice.polytech.si5.soa.a.controllers;

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
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

/**
 * Class name	RestaurantController
 * Date			23/10/2018
 * @author		PierreRainero
 *
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	private static Logger logger = LogManager.getLogger(RestaurantController.class);
	
	@Autowired
	private IRestaurantService restaurantService;
	
	@Autowired
	private IMealService mealService;
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addRestaurant(@RequestBody RestaurantDTO restaurant) {
		return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
	}
	
	@RequestMapping(value = "/{restaurantId}/meals",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addMeal(@PathVariable("restaurantId") String restaurantId, @RequestBody MealDTO meal) {
		int convertedId = Integer.parseInt(restaurantId);
		
		try {
			return ResponseEntity.ok(mealService.addMeal(meal, convertedId));
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
