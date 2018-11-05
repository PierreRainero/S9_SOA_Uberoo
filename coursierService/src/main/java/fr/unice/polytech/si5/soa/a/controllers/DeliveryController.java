package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.message.CourseCancelMessage;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.message.NewOrder;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

/**
 * Class name	DeliveryController
 * Date			08/10/2018
 *
 * @author PierreRainero
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private static Logger logger = LogManager.getLogger(DeliveryController.class);

    @Autowired
    private IDeliveryService deliveryService;

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            consumes = {"application/JSON; charset=UTF-8"},
            produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<?> updateDeliveryState(@RequestBody DeliveryDTO delivery) {
        try {
            return ResponseEntity.ok(deliveryService.updateDelivery(delivery));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{deliveryId}",
            method = RequestMethod.PUT,
            consumes = {"application/JSON; charset=UTF-8"},
            produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<?> updateDelivery(@RequestParam("coursierId") Integer coursierId,
                                            @PathVariable("deliveryId") Integer deliveryId,
                                            @RequestBody CourseCancelMessage courseCancelMessage) {
        try{
            if (courseCancelMessage.isNull()) {
                return ResponseEntity.ok(this.deliveryService.assignDelivery(deliveryId, coursierId));
            } else {
                return ResponseEntity.ok(this.deliveryService.replaceOrder(courseCancelMessage.createCancelDataDTO()));
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<?> getDeliveriesToDo(@QueryParam("latitude") Double latitude, @QueryParam("longitude") Double longitude) {
        if (latitude != null && longitude != null) {
            return ResponseEntity.ok(deliveryService.getDeliveriesToDo(latitude, longitude));
        }
        return ResponseEntity.ok(deliveryService.getDeliveriesToDo());
    }
}
