package fr.unice.polytech.si5.soa.a.communication.DTO;

import fr.unice.polytech.si5.soa.a.communication.message.CourseCancelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode()
@ToString()
public class CancelDataDTO {

    private Integer coursierId;
    private Integer deliveryId;
    private String reasonType;
    private String explanations;

    public CancelDataDTO() {

    }

    public CancelDataDTO(String reasonType, String explanations, Integer coursierId, Integer deliveryId) {
        this.reasonType = reasonType;
        this.explanations = explanations;
        this.coursierId = coursierId;
        this.deliveryId = deliveryId;
    }

    public CourseCancelMessage createCourseCancelMessage() {
        return new CourseCancelMessage(coursierId, deliveryId, reasonType, explanations);
    }

}
