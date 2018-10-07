package fr.unice.polytech.si5.soa.a.restaurantservice.Controller;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {
    @Autowired
    private StatRepository statRepository;

    @GetMapping(path = "")
    @ResponseBody
    public List<Stat> getStats() {
        return statRepository.findAll();
    }
}
