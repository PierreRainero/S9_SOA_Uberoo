package fr.unice.polytech.si5.soa.a.controllers;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;

/**
 * Class name	CatalogController
 * Date			01/10/2018
 * @author		PierreRainero
 * 
 * @version		1.1
 * Date			21/10/2018
 */
@RestController
@RequestMapping("")
public class CatalogController {
	private static Logger logger = LogManager.getLogger(CatalogController.class);
	private final static String HEADERS = "application/JSON; charset=UTF-8";
	
	private final static String BASE_URI = "/meals";
	
	@Autowired
	private ICatalogService catalogService;
	
	@RequestMapping(value = BASE_URI,
			method = RequestMethod.GET,
			produces = {HEADERS})
	public ResponseEntity<?> findMealsByTag(@RequestParam("tag") Optional<String> tag) {
		if(!tag.isPresent()) {
			return ResponseEntity.ok(catalogService.findMealsByTag(""));
		}else {
			return ResponseEntity.ok(catalogService.findMealsByTag(tag.get()));
		}
	}
	
	@RequestMapping(value = "/restaurants/{restaurantId}"+BASE_URI,
			method = RequestMethod.GET,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> findMealsByRestaurant(@PathVariable("restaurantId") String id) {
		int convertedId = Integer.parseInt(id);
		
		try {
			return ResponseEntity.ok(catalogService.findMealsByRestaurant(convertedId));
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = BASE_URI+"/{mealId}/feedbacks",
			method = RequestMethod.POST,
			consumes = {HEADERS},
			produces = {HEADERS})
	public ResponseEntity<?> addFeedback(
			@PathVariable("mealId") String mealId,
			@RequestBody FeedbackDTO feedback) {
		int mealIdAsInt = Integer.parseInt(mealId);
		int authorId = -1;
		
		if(feedback.getAuthor() != null) {
			authorId = feedback.getAuthor().getId();
		}else {
			return ResponseEntity.status(400).body("Malformed request.");
		}
				
		try {
			return ResponseEntity.ok(catalogService.addFeedback(feedback, authorId, mealIdAsInt));
		} catch (UnknowMealException | UnknowUserException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
