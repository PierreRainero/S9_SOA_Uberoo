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


    @GetMapping(value="/restaurant")
    public String restaurant() {
        return "Hello World !!";
    }

    @GetMapping(value = "/restaurant/{id}")
    public String getRestaurantById(@PathVariable int id) {
        return "Vous avez demandé le restaurant " + id;
    }

    @GetMapping(value = "/restaurant/{id}/orders")
    public Order getRestaurantOrders(@PathVariable int id) {
        String [] items = {"coca", "kebab"};
        Order o = new Order(13, items);
        return o;
    }

    @PostMapping(value= "/restaurant/{id}/addOrder")
    public int addOrder(@PathVariable int id)
    {
        return 400;
    }

}
