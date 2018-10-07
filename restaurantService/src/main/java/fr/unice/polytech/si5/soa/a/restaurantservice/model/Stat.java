package fr.unice.polytech.si5.soa.a.restaurantservice.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.persistence.GeneratedValue;

@Entity
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;
}
