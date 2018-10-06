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

import fr.unice.polytech.si5.soa.a.dto.OrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;

/**
 * Class name	OrderTakerController
 * Date			29/09/2018
 * @author		PierreRainero
 */
@RestController
@RequestMapping("/orders")
public class OrderTakerController {
	private static Logger logger = LogManager.getLogger(OrderTakerController.class);
	
	@Autowired
	private IOrderTakerService orderService;

	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addOrder(@RequestBody OrderDTO order) {
		try {
			return ResponseEntity.ok(orderService.addOrder(order));
		}catch(UnknowUserException | UnknowMealException e) {
			logger.info(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}catch(EmptyDeliveryAddressException e) {
			logger.info(e.getMessage(), e);
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/{orderId}/",
			method = RequestMethod.PUT,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> updateOrderState(@PathVariable("orderId") String id, @RequestBody OrderDTO order) {
		try {
			return ResponseEntity.ok(orderService.updateOrderState(order));
		}catch(UnknowOrderException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
