package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewFeedback
 * Date			29/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewFeedback extends Message {
	private String author;
	private String content;
	private String mealName;
	private String restaurantName;
	private String restaurantAddress;
	
	/**
	 * Default constructor
	 */
	public NewFeedback() {
		// Default constructor for Jackson databinding
	}
	
	public NewFeedback(FeedbackDTO feedbackData, MealDTO mealData) {
		super();
		
		type = "NEW_FEEDBACK";
		
		author = feedbackData.getAuthor().getFirstName() + ' ' + feedbackData.getAuthor().getLastName();
		content = feedbackData.getContent();
		mealName = mealData.getName();
		restaurantName = mealData.getRestaurant().getName();
		restaurantAddress = mealData.getRestaurant().getRestaurantAddress();
	}
}
