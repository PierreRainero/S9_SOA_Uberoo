package fr.unice.polytech.si5.soa.a.repositories;

import fr.unice.polytech.si5.soa.a.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

    public List<Delivery> findAll();

}
