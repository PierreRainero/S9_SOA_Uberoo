package fr.unice.polytech.si5.soa.a.entities;


import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import javax.persistence.*;

import java.io.Serializable;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

@Entity
@Data
@Table(name = "`FEEDBACKS`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Restaurant restaurant;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Meal meal;

    public Feedback() {

    }

}
