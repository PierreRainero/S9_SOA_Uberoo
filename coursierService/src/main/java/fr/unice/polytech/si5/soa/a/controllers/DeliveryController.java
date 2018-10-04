package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.services.ICatalogService;

/**
 * Class name	DeliveryController
 * Date			03/10/2018
 * @author		AlexisDeslandes
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<List<Delivery>> findDeliveries() {
        return ResponseEntity.ok(deliveryService.findTobeDeliveredDeliveries());
    }
}
