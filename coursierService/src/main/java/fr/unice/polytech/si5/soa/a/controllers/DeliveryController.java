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

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.NewOrder;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowDeliveryException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;

/**
 * Class name	DeliveryController
 * Date			08/10/2018
 * @author		PierreRainero
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
	private static Logger logger = LogManager.getLogger(DeliveryController.class);
	
	@Autowired
	private IDeliveryService deliveryService;

	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addOrder(@RequestBody NewOrder order) {
		DeliveryDTO delivery = order.createDelivery();
		
		return ResponseEntity.ok(deliveryService.addDelivery(delivery));
	}
	
	@RequestMapping(value = "/{deliveryId}/",
			method = RequestMethod.PUT,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> updateOrderState(@PathVariable("deliveryId") String id, @RequestBody DeliveryDTO delivery) {
		try {
			return ResponseEntity.ok(deliveryService.updateDelivery(delivery));
		}catch(UnknowDeliveryException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "",
			method = RequestMethod.GET,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> getDeliveriesToDo() {
		return ResponseEntity.ok(deliveryService.getDeliveriesToDo());
	}
}
