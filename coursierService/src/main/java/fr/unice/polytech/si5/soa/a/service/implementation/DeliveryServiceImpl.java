package fr.unice.polytech.si5.soa.a.service.implementation;

import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.repositories.DeliveryRepository;
import fr.unice.polytech.si5.soa.a.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the delivery service.
 * @author Alexis Deslandes
 */
@Service
public class DeliveryServiceImpl implements IDeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    /**
     * @return all the deliveries that have not been delivered yet.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Delivery> findTobeDeliveredDeliveries() {
        return this.deliveryRepository.findAll().stream().filter(Delivery::getToBeDelivered).collect(Collectors.toList());
    }

    /**
     * Update the state of the delivery with the corresponding idDelivery.
     * After the call of this method, the delivery is delivered.
     * @param idDelivery Id of the delivery.
     * @throws Exception If the delivery has not been found.
     */
    @Override
    public void updateDeliveryToDelivered(Long idDelivery) throws Exception {
        Optional<Delivery> optionalDelivery = this.deliveryRepository.findById(idDelivery);
        if (optionalDelivery.isPresent()){
            Delivery delivery = optionalDelivery.get();
            delivery.setToBeDelivered(true);
            deliveryRepository.save(delivery);
        }else{
            throw new Exception("Delivery not found.");
        }
    }
}
