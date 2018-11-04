package fr.unice.polytech.si5.soa.a.communication.message;

import fr.unice.polytech.si5.soa.a.communication.DTO.CancelDataDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class CourseCancelMessage extends Message {

    private Integer coursierId;
    private Integer deliveryId;
    private String reasonType;
    private String explanations;

    public static String messageType = "COURSE_CANCEL";

    public CourseCancelMessage() {
        type = messageType;
    }

    public CourseCancelMessage(Integer coursierId, Integer deliveryId, String reasonType, String explanations) {
        this();
        this.coursierId = coursierId;
        this.deliveryId = deliveryId;
        this.reasonType = reasonType;
        this.explanations = explanations;
    }

    public CancelDataDTO createCancelDataDTO() {
        return new CancelDataDTO(reasonType, explanations, coursierId, deliveryId);
    }

    public boolean isNull() {
        return coursierId == null && deliveryId == null && reasonType == null && explanations == null;
    }

}
