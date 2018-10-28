package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Delivery;

/**
 * Class name	IDeliveryDao
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IDeliveryDao {
	Delivery addDelivery(Delivery deliveryToAdd);
	Delivery updateDelivery(Delivery deliveryToUpdate);
	Optional<Delivery> findDeliveryById(int id);
	List<Delivery> getDeliveriesToDo();

    List<Delivery> getDeliveriesDoneBy(Integer idCoursier);
}
