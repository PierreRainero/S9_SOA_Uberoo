package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class CoursierDto {

    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;


    public CoursierDto() {
    }


    public CoursierDto(int id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
