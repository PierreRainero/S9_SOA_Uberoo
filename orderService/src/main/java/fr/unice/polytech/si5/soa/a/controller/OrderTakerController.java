package fr.unice.polytech.si5.soa.a.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
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

	@RequestMapping(value = "/",
			method = RequestMethod.POST,
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<CommandDTO> addUser(@RequestBody CommandDTO command) {
		return ResponseEntity.ok(orderService.addCommand(command));
	}
}
