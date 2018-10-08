package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Delivery;

public interface IDeliveryDao {
	Delivery addDelivery(Delivery deliveryToAdd);
	Delivery updateDelivery(Delivery deliveryToUpdate);
	Optional<Delivery> findDeliveryById(int id);
	List<Delivery> getDeliveriesToDo();
}
