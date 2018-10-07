package fr.unice.polytech.si5.soa.a.restaurantservice.controllers;

import fr.unice.polytech.si5.soa.a.restaurantservice.communication.NewOrder;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;
import fr.unice.polytech.si5.soa.a.restaurantservice.services.component.OrderServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestaurantController {
	private OrderServiceImpl orderService = new OrderServiceImpl();

	@RequestMapping(value = "/restaurants/orders",
					method = RequestMethod.GET,
					produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> getRestaurantOrders() {
		return ResponseEntity.ok(orderService.getOrders());
		
	}

	@RequestMapping(value = "/restaurants/orders", 
					method = RequestMethod.POST,
					consumes = {"application/JSON; charset=UTF-8"},
					produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> newOrder(@RequestBody NewOrder messageOrder) {
		OrderToPrepare o = messageOrder.getOrder();
		boolean result = orderService.addOrder(o);
		
		if(result) {
			return ResponseEntity.ok("{\"status\": \"ok\"}");
		}else {
			return ResponseEntity.status(500).body("{\"status\": \"ok\"}");
		}	
	}
}
