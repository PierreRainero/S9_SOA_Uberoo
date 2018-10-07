package fr.unice.polytech.si5.soa.a.restaurantservice.controllers;

import fr.unice.polytech.si5.soa.a.restaurantservice.communication.NewOrder;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;
import fr.unice.polytech.si5.soa.a.restaurantservice.services.component.OrderServiceImpl;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {
	private OrderServiceImpl orderService = new OrderServiceImpl();

	@RequestMapping(value = "restaurants/orders",
					method = RequestMethod.GET,
					produces = {"application/JSON; charset=UTF-8"})
	public List<OrderToPrepare> getRestaurantOrders() {
		return orderService.getOrders();
		
	}

	@RequestMapping(value = "restaurants/orders", 
					method = RequestMethod.POST,
					consumes = {"application/JSON; charset=UTF-8"},
					produces = {"application/JSON; charset=UTF-8"})
	public String newOrder(@RequestBody NewOrder messageOrder) {
		OrderToPrepare o = messageOrder.getOrder();
		String result = orderService.addOrder(o);
		return "{\"status\": "+result+"}";
	}
}
