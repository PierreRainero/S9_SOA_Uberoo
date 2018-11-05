package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.DTO.CancelDataDTO;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.message.OrderDelivered;
import fr.unice.polytech.si5.soa.a.communication.message.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.*;
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
    private IRestaurantDao restaurantDao;

    @Autowired
    private MessageProducer messageProducer;

    @Override
    public DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd) throws UnknownRestaurantException, NoAvailableCoursierException {
    	Delivery delivery = new Delivery(deliveryToAdd);
    	Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurant(deliveryToAdd.getRestaurant().getName(), deliveryToAdd.getRestaurant().getAddress());
    	if(!restaurantWrapped.isPresent()) {
        	throw new UnknownRestaurantException("Cant find restaurant");
        }
        delivery.setRestaurant(restaurantWrapped.get());
        
        Optional<Coursier> coursierWrapped = coursierDao.getNearestCoursier(delivery.getDeliveryAddress());
        if(!coursierWrapped.isPresent()) {
        	throw new NoAvailableCoursierException("Cant find coursier near to the delivery address");
        }
        delivery.setCoursier(coursierWrapped.get());
        
    	return deliveryDao.addDelivery(delivery).toDTO();
    }

    @Override
    public DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknownCoursierException, UnknownDeliveryException {
        Optional<Delivery> deliveryWrapped = deliveryDao.findDeliveryById(deliveryToUpdate.getId());
        if (!deliveryWrapped.isPresent()) {
            throw new UnknownDeliveryException(deliveryToUpdate.getId());
        }
        Delivery delivery = deliveryWrapped.get();

        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(delivery.getCoursier().getId());
        if (!coursierWrapped.isPresent()){
            throw new UnknownCoursierException(deliveryToUpdate.getCoursier().getId().toString());
        }
        Coursier coursier = coursierWrapped.get();
        coursier.updateDelivery(delivery);
        delivery.setState(deliveryToUpdate.isState());
        delivery.setDeliveryDate(new Date());
	    OrderDelivered orderDelivered = new OrderDelivered();
        orderDelivered.setAddress(delivery.getDeliveryAddress());
        orderDelivered.setDate(new Date());
        orderDelivered.setDeliveryId(delivery.getId());
        orderDelivered.setAccount(coursier.getAccountNumber());
        orderDelivered.setFood(delivery.getFood());
        orderDelivered.setAmount(5.0);//Comission pour un coursier sous exploité
        orderDelivered.setRestaurantName(delivery.getRestaurant().getName());
        orderDelivered.setRestaurantAddress(delivery.getRestaurant().getAddress());
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

        delivery.setCoursier(coursier);
        delivery.setCreationDate(new Date());
        coursier.addDelivery(delivery);
        this.coursierDao.updateCoursier(coursier);
        return this.deliveryDao.updateDelivery(delivery).toDTO();
    }

    @Override
    public DeliveryDTO replaceOrder(CancelDataDTO courseCancelMessage) throws UnknownDeliveryException, UnknownCoursierException {
        Integer deliveryId = courseCancelMessage.getDeliveryId();
        Optional<Delivery> deliveryWrapped = this.deliveryDao.findDeliveryById(deliveryId);
        if (deliveryWrapped.isPresent()) {
            Integer coursierId = courseCancelMessage.getCoursierId();
            Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(coursierId);
            if (coursierWrapped.isPresent()) {
                Delivery delivery = deliveryWrapped.get();
                Coursier coursier = coursierWrapped.get();
                coursier.removeDelivery(delivery);
                delivery.setCancel(true);
                coursierDao.updateCoursier(coursier);
                deliveryDao.updateDelivery(delivery);
                messageProducer.sendMessage(courseCancelMessage.createCourseCancelMessage());
                return delivery.toDTO();
            } else {
                throw new UnknownCoursierException(coursierId.toString());
            }
        } else {
            throw new UnknownDeliveryException(deliveryId);
        }
    }
}
