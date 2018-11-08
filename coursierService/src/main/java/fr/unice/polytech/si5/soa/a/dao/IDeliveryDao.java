package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Delivery;

/**
 * Class name	IDeliveryDao
 * Date			08/10/2018
 *
 * @author PierreRainero
 */
public interface IDeliveryDao {
    /**
     * Add delivery to the service.
     *
     * @param deliveryToAdd The delivery to add
     * @return The delivery added.
     */
    Delivery addDelivery(Delivery deliveryToAdd);

    /**
     * Update the following delivery.
     *
     * @param deliveryToUpdate Next state delivery
     * @return Delivery which has been updated.
     */
    Delivery updateDelivery(Delivery deliveryToUpdate);

    /**
     * Find the delivery with the corresponding id.
     *
     * @param id Id of the delivery
     * @return The corresponding delivery
     */
    Optional<Delivery> findDeliveryById(int id);

    /**
     * Get the deliveries which has not been done.
     *
     * @return The corresponding list of delivery
     */
    List<Delivery> getDeliveriesToDo();

    /**
     * Get the deliveries done by the coursier with the corresponding id.
     *
     * @param idCoursier Id of the coursier.
     * @return The corresponding list of delivery.
     */
    List<Delivery> getDeliveriesDoneBy(Integer idCoursier);
}
