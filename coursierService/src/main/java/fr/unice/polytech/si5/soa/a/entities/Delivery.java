package fr.unice.polytech.si5.soa.a.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;

@Entity
@Table(name="allocation_view")
@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonIgnoreProperties
public class Delivery {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "to_be_delivered")
    private Boolean toBeDelivered;

    public Boolean getToBeDelivered() {
        return toBeDelivered;
    }

    public void setToBeDelivered(Boolean toBeDelivered) {
        this.toBeDelivered = toBeDelivered;
    }
}
