package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	FeedbackDTO
 * Date			29/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class FeedbackDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 5759766349529630488L;
	
	@Column(name = "author", nullable = false)
    private UserDTO author;

    @Column(name = "content", nullable = false)
    private String content;

    /**
     * Default constructor
     */
    public FeedbackDTO() {
    	// Default constructor for Jackson databinding
    }
    
    public FeedbackDTO(UserDTO author, String content) {
    	this.author = author;
    	this.content = content;
    }
}
