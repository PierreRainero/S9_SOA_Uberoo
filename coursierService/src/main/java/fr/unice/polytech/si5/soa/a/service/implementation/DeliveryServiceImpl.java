package fr.unice.polytech.si5.soa.a.service.implementation;

import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.repositories.DeliveryRepository;
import fr.unice.polytech.si5.soa.a.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryServiceImpl implements IDeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Delivery> findTobeDeliveredDeliveries() {
        return this.deliveryRepository.findAll().stream().filter(Delivery::getToBeDelivered).collect(Collectors.toList());
    }
}
