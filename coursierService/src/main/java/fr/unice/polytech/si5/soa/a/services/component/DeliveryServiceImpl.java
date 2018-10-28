package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.OrderDelivered;
import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowDeliveryException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.message.MessageProducer;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import fr.unice.polytech.si5.soa.a.utils.Geoposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class name	DeliveryServiceImpl
 *
 * @author PierreRainero
 * @see IDeliveryService Date			08/10/2018
 */
@Primary
@Service("DeliveryService")
public class DeliveryServiceImpl implements IDeliveryService {

    @Autowired
    private IDeliveryDao deliveryDao;

    @Autowired
    private MessageProducer messageProducer;

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
        //TODO: orderDelivered could have others attributes
	    OrderDelivered orderDelivered = new OrderDelivered(delivery.getDeliveryAddress(),delivery.getId());
        messageProducer.sendMessage(orderDelivered);

        return deliveryDao.updateDelivery(delivery).toDTO();
    }

    @Override
    public List<DeliveryDTO> getDeliveriesToDo() {
        return deliveryDao.getDeliveriesToDo().stream().map(Delivery::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DeliveryDTO> getDeliveriesToDo(Double latitude, Double longitude) {
        return this.getDeliveriesToDo().stream()
                .filter(deliveryDTO -> Geoposition.distance(latitude, deliveryDTO.getLatitude(), longitude, deliveryDTO.getLongitude()) < Geoposition.DISTANCE_MAX_DELIVERY)
                .collect(Collectors.toList());
    }

    @Override
    public void receiveNewPayment(PaymentConfirmation message) throws UnknownDeliveryException {
        boolean coursierGetPaid = message.isStatus();
        if (coursierGetPaid){
            Optional<Delivery> deliveryWrapped = this.deliveryDao.findDeliveryById(message.getId());
            if (deliveryWrapped.isPresent()){
                Delivery delivery = deliveryWrapped.get();
                delivery.setCoursierGetPaid(true);
                deliveryDao.updateDelivery(delivery);
                System.out.println("The payment has been received by the coursier");
            }else{
                throw new UnknownDeliveryException("Can't find the delivery with id : " + message.getId());
            }
        }else{
            System.out.println("The payment has not been confirmed : " + message.getId());
        }
    }
}
