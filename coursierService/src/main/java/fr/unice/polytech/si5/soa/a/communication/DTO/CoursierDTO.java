package fr.unice.polytech.si5.soa.a.communication.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class CoursierDTO {

    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;


    public CoursierDTO() {
    }


    public CoursierDTO(int id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
