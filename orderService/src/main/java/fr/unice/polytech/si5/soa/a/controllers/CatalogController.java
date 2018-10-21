package fr.unice.polytech.si5.soa.a.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
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
	
	private final static String BASE_URI = "/meals/";
	
	@Autowired
	private ICatalogService catalogService;
	
	@RequestMapping(value = BASE_URI,
			method = RequestMethod.GET,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> findMealsByTag(@RequestParam("tag") String tag) {
		return ResponseEntity.ok(catalogService.findMealsByTag(tag));
	}
	
	@RequestMapping(value = "/restaurants/{restaurantId}"+BASE_URI,
			method = RequestMethod.GET,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> findMealsByRestaurant(@PathVariable("restaurantId") String id) {
		int convertedId = Integer.parseInt(id);
		
		try {
			return ResponseEntity.ok(catalogService.findMealsByRestaurant(convertedId));
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
