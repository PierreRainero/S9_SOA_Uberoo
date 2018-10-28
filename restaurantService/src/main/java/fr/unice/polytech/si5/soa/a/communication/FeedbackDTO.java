package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	FeedbackDTO
 * Date			28/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class FeedbackDTO {
    private String author;
    private String content;
    private MealDTO meal;
    
    public FeedbackDTO() {
    	
    }
    
    public FeedbackDTO(String author, String content, MealDTO meal) {
    	this.author = author;
    	this.content = content;
    	this.meal = meal;
    }
}
