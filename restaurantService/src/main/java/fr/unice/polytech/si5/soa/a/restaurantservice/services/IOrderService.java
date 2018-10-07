package fr.unice.polytech.si5.soa.a.restaurantservice.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;

public interface IOrderService {
	boolean addOrder(OrderToPrepare orderToAdd);
	List<OrderToPrepare> getOrders();
}
