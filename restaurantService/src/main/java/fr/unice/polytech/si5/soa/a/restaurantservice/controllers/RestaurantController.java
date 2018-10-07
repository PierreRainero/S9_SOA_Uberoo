package fr.unice.polytech.si5.soa.a.restaurantservice.controllers;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@controllers
@RestController
//@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private StatRepository statRepository;


    /*@GetMapping(value="/restaurants")
    public String restaurant() {
        return "Hello World !!";
    }

    @GetMapping(value = "/restaurants/{id}")
    public String getRestaurantById(@PathVariable int id) {
        return "Vous avez demandé le restaurant " + id;
    }*/

    @GetMapping(value = "/orders")
    public Order getRestaurantOrders(@PathVariable int id) {
        String [] items = {"coca", "kebab"};
        Order o = new Order(0, items);
        return o;
    }
}
