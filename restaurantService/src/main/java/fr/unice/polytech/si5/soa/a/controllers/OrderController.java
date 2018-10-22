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

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.services.IOrderService;

/**
 * Class name	OrderController
 * Date			08/10/2018
 * @author		PierreRainero
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
public class OrderController {
	private static Logger logger = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping(value = "/{orderId}/",
			method = RequestMethod.PUT,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> updateOrderState(
			@PathVariable("restaurantId") String restaurantId,
			@PathVariable("orderId") String id,
			@RequestBody RestaurantOrderDTO order) {
		try {
			return ResponseEntity.ok(orderService.updateOrder(order));
		}catch(UnknowOrderException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> getDeliveriesToDo(@PathVariable("restaurantId") String restaurantId) {
		return ResponseEntity.ok(orderService.getOrdersToDo());
	}
}
