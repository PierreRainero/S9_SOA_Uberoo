package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
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
public class NewFeedback extends Message{
	public static String messageType = "NEW_FEEDBACK";
	
	private String author;
	private String content;
	private String mealName;
	private String restaurantName;
	private String restaurantAddress;
	
	/**
	 * Default constructor
	 */
	public NewFeedback() {
		super();
		type = messageType;
	}
	
	public FeedbackDTO createFeedback() {
		return new FeedbackDTO(author, content, null);
	}
}
