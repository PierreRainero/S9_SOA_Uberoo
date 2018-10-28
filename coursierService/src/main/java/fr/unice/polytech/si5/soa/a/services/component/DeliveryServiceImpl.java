package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.OrderDelivered;
import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.message.MessageProducer;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import fr.unice.polytech.si5.soa.a.utils.Geoposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private ICoursierDao coursierDao;

    @Autowired
    private MessageProducer messageProducer;

    @Override
    public DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd) {
        return deliveryDao.addDelivery(new Delivery(deliveryToAdd)).toDTO();
    }

    @Override
    public DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknownCoursierException, UnknownDeliveryException {
        Optional<Delivery> deliveryWrapped = deliveryDao.findDeliveryById(deliveryToUpdate.getId());
        if (!deliveryWrapped.isPresent()) {
            throw new UnknownDeliveryException(deliveryToUpdate.getId());
        }
        Delivery delivery = deliveryWrapped.get();

        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(delivery.getCoursierId());
        if (!coursierWrapped.isPresent()){
            throw new UnknownCoursierException(delivery.getCoursierId().toString());
        }
        Coursier coursier = coursierWrapped.get();
        coursier.updateDelivery(delivery);
        delivery.setState(deliveryToUpdate.isState());
        delivery.setDeliveryDate(new Date());
	    OrderDelivered orderDelivered = new OrderDelivered()
                .addAddress(delivery.getDeliveryAddress())
                .addDate(new Date())
                .addDeliveryId(delivery.getId())
                .addAccount(coursier.getAccountNumber());

        messageProducer.sendMessage(orderDelivered);
        coursierDao.updateCoursier(coursier);
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
    public Delivery receiveNewPayment(PaymentConfirmation message) throws UnknownDeliveryException, CoursierDoesntGetPaidException {
        boolean coursierGetPaid = message.isStatus();
        if (coursierGetPaid){
            Optional<Delivery> deliveryWrapped = this.deliveryDao.findDeliveryById(message.getId());
            if (deliveryWrapped.isPresent()){
                Delivery delivery = deliveryWrapped.get();
                delivery.setCoursierGetPaid(true);
                delivery = deliveryDao.updateDelivery(delivery);
                System.out.println("The payment has been received by the coursier");
                return delivery;
            }else{
                throw new UnknownDeliveryException(message.getId());
            }
        }else{
            throw new CoursierDoesntGetPaidException(message.getId());
        }
    }

    @Override
    public DeliveryDTO assignDelivery(Integer deliveryId, Integer coursierId) throws UnknownDeliveryException, UnknownCoursierException {
        Optional<Delivery> deliveryWrapped = this.deliveryDao.findDeliveryById(deliveryId);
        if (!deliveryWrapped.isPresent()){
            throw new UnknownDeliveryException(deliveryId);
        }
        Delivery delivery = deliveryWrapped.get();

        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(coursierId);
        if (!coursierWrapped.isPresent()){
            throw new UnknownCoursierException(coursierId.toString());
        }
        Coursier coursier = coursierWrapped.get();

        delivery.setCoursierId(coursierId);
        delivery.setCreationDate(new Date());
        coursier.addDelivery(delivery);
        this.coursierDao.updateCoursier(coursier);
        return this.deliveryDao.updateDelivery(delivery).toDTO();
    }
}
