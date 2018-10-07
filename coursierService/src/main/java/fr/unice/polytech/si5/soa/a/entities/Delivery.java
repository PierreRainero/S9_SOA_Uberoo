package fr.unice.polytech.si5.soa.a.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import fr.unice.polytech.si5.soa.a.dto.OrderDTO;

import javax.persistence.*;

/**
 * Class representing a delivery in the system.
 * @author Alexis Deslandes
 */
@Entity
@Table(name="delivery")
@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonIgnoreProperties
public class Delivery {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

//    @Column(name="id_order")
//    private Long idOrder;

    @Column(name = "to_be_delivered")
    private Boolean toBeDelivered;

    @Column(name = "uberooorder")
    private OrderDTO orderDTO;

    public Boolean getToBeDelivered() {
        return toBeDelivered;
    }

    public void setToBeDelivered(Boolean toBeDelivered) {
        this.toBeDelivered = toBeDelivered;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

//    public Long getIdOrder() {
//        return idOrder;
//    }
//
//    public void setIdOrder(Long idOrder) {
//        this.idOrder = idOrder;
//    }
}
