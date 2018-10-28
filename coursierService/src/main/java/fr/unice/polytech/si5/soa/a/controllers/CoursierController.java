package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.exceptions.UnknowCoursierException;
import fr.unice.polytech.si5.soa.a.services.ICoursierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/coursiers")
public class CoursierController {

    private static Logger logger = LogManager.getLogger(DeliveryController.class);

    @Autowired
    private ICoursierService coursierService;

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/JSON; charset=UTF-8"})
    public ResponseEntity<?> getCoursier(@QueryParam("id") Integer idCoursier) {
        try {
            return ResponseEntity.ok(this.coursierService.getCoursier(idCoursier));
        } catch (UnknowCoursierException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
