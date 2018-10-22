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

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import fr.unice.polytech.si5.soa.a.services.IPaymentService;

/**
 * 
 * Class name	PaymentController
 * Date			22/10/2018
 * @author		PierreRainero
 *
 */
@RestController
@RequestMapping("")
public class PaymentController {
	private static Logger logger = LogManager.getLogger(PaymentController.class);
	
	private final static String BASE_URI = "/payments";
	
	@Autowired
	private IPaymentService paymentService;
	
	@RequestMapping(value = "/orders/{orderId}"+BASE_URI,
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addPayment(@PathVariable("orderId") String id, @RequestBody PaymentDTO payment) {
		int convertedId = Integer.parseInt(id);
		
		try {
			return ResponseEntity.ok(paymentService.addPayment(payment, convertedId));
		} catch (UnknowOrderException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
