package fr.unice.polytech.si5.soa.a.restaurantservice.dao;

import java.util.List;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;

public interface IOrderDao {
	OrderToPrepare addOrder(OrderToPrepare orderToAdd);
	
	List<OrderToPrepare> getOrders();
}
