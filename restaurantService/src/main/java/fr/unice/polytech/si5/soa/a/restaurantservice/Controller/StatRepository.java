package fr.unice.polytech.si5.soa.a.restaurantservice.Controller;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, Integer> {

}

