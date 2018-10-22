package fr.unice.polytech.si5.soa.a.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

/**
 * 
 * Class name	RestaurantController
 * Date			20/10/2018
 * @author		PierreRainero
 *
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	@Autowired
	private IRestaurantService restaurantService;
	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> findMealsByTag(@RequestParam("name") Optional<String> name) {
		if(!name.isPresent()) {
			return ResponseEntity.ok(restaurantService.findRestaurantByName(""));
		}else {
			return ResponseEntity.ok(restaurantService.findRestaurantByName(name.get()));
		}
	}
}
