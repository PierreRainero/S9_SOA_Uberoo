package fr.unice.polytech.si5.soa.a.restaurantservice.controllers;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.Order;
import fr.unice.polytech.si5.soa.a.restaurantservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@controllers
@RestController
//@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private StatRepository statRepository;

    private OrderService orderService = new OrderService();


    /*@GetMapping(value="/restaurants")
    public String restaurant() {
        return "Hello World !!";
    }

    @GetMapping(value = "/restaurants/{id}")
    public String getRestaurantById(@PathVariable int id) {
        return "Vous avez demandé le restaurant " + id;
    }*/

    @GetMapping(value = "restaurants/orders")
    public List<Order> getRestaurantOrders() {
        return orderService.getOrders();
    }

    @RequestMapping(value = "restaurants/new_order", method = RequestMethod.POST, consumes = {"application/JSON; charset=UTF-8"},
                                                    produces = {"application/JSON; charset=UTF-8"})
    public String newOrder(@RequestBody Order o) {
        orderService.addOrder(o);
        return "ok";
    }
}
