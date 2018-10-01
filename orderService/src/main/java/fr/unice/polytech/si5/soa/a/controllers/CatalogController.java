package fr.unice.polytech.si5.soa.a.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.services.ICatalogService;

/**
 * Class name	CatalogController
 * Date			01/10/2018
 * @author		PierreRainero
 */
@RestController
@RequestMapping("/meals")
public class CatalogController {
	
	@Autowired
	private ICatalogService catalogService;
	
	@RequestMapping(value = "/",
			method = RequestMethod.GET,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> findMealsByTag(@RequestParam("tag") String tag) {
		return ResponseEntity.ok(catalogService.findMealsByTag(tag));
	}
}
