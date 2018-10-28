package fr.unice.polytech.si5.soa.a.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class Coursier
 *
 * @author Joël CANCELA VAZ
 */
@Entity
@Data
@Table(name = "`DELIVERY`")
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class Coursier {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "account_number", nullable = false)
    private String account_number;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public Coursier() {
    }
}