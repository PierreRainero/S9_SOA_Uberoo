package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class name	DeliveryController
 * Date			03/10/2018
 *
 * @author AlexisDeslandes
 */
@RestController
@RequestMapping("/deliveries")
@Transactional()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<List<Delivery>> findDeliveries() {
        return new ResponseEntity<>(deliveryService.findTobeDeliveredDeliveries(), HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<Void> updateDeliveryToDelivered(@RequestParam(value = "idDelivery", required = true) Long idDelivery) {
        try{
            this.deliveryService.updateDeliveryToDelivered(idDelivery);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
