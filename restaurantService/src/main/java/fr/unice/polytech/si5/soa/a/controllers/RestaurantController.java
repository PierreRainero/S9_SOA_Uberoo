package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.InvalidOrderIDException;
import fr.unice.polytech.si5.soa.a.exceptions.NoMealException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Class name	OrderTakerController
 * Date			29/09/2018
 *
 * @author PierreRainero
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	private static Logger logger = LogManager.getLogger(RestaurantController.class);

	@Autowired
	private IOrderTakerService orderService;

	@RequestMapping(value = "/orders",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addOrder(@RequestBody OrderDTO order) {
		try {
			return ResponseEntity.ok(orderService.addOrder(order));
		} catch (NoMealException e) {
			logger.info(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/orders",
			method = RequestMethod.GET,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> updateOrderState() {
		return ResponseEntity.ok(orderService.getOrdersToDo());
	}

	@RequestMapping(value = "/orders/{id}",
			method = RequestMethod.PUT,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> completeOrder(@PathVariable("orderId") int id) {
		try {
			return ResponseEntity.ok(orderService.markOrderAsComplete(id));
		} catch (InvalidOrderIDException e) {
			logger.info(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
