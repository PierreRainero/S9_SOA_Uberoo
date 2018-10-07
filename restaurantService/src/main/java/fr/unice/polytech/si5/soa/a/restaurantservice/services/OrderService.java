package fr.unice.polytech.si5.soa.a.restaurantservice.services;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Primary
@Service("OrderService")
public class OrderService {

    //private Map<Integer, List<Order>> ordersByResto; // int: restoId      Order[]: this resto orders
    private List<Order> orders;

    public OrderService() {
        //ordersByResto = new HashMap<>();
        orders = new ArrayList<>();
    }

    /*public List<Order> findOrdersByRestoId(int id) {
        return ordersByResto.get(id);
    }*/

    public void addOrder(Order o) {
        /*if (!ordersByResto.containsKey(id))
        {
            ordersByResto.put(id, new ArrayList<>());
        }
        ordersByResto.get(id).add(o);
        return "ok";*/
        orders.add(o);
    }

    public List<Order> getOrders() {
        return orders;
    }

}