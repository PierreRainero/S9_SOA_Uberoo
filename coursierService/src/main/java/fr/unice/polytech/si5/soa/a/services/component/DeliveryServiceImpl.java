package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowDeliveryException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;

/**
 * Class name	DeliveryServiceImpl
 *
 * @see            IDeliveryService Date			08/10/2018
 * @author PierreRainero
 */
@Primary
@Service("DeliveryService")
public class DeliveryServiceImpl implements IDeliveryService {
    @Autowired
    private IDeliveryDao deliveryDao;

    @Override
    public DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd) {
        return deliveryDao.addDelivery(new Delivery(deliveryToAdd)).toDTO();
    }

    @Override
    public DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknowDeliveryException {
        Optional<Delivery> deliveryWrapped = deliveryDao.findDeliveryById(deliveryToUpdate.getId());
        if (!deliveryWrapped.isPresent()) {
            throw new UnknowDeliveryException("Can't find delivery with id = " + deliveryToUpdate.getId());
        }
        Delivery delivery = deliveryWrapped.get();
        delivery.setState(deliveryToUpdate.isState());

        return deliveryDao.updateDelivery(delivery).toDTO();
    }

    @Override
    public List<DeliveryDTO> getDeliveriesToDo() {
        return deliveryDao.getDeliveriesToDo().stream().map(delivery -> delivery.toDTO()).collect(Collectors.toList());
    }
}
