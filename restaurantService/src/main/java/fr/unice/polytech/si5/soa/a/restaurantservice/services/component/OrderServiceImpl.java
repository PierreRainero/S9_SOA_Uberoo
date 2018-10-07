package fr.unice.polytech.si5.soa.a.restaurantservice.services.component;

import fr.unice.polytech.si5.soa.a.restaurantservice.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;
import fr.unice.polytech.si5.soa.a.restaurantservice.services.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service("OrderService")
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private IOrderDao orderdao;

	@Override
	public boolean addOrder(OrderToPrepare orderToAdd) {
		OrderToPrepare otp = orderdao.addOrder(orderToAdd);
		if(otp!=null && otp.getId()>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<OrderToPrepare> getOrders() {
		return orderdao.getOrders();
	}
}