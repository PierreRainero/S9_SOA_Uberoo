package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;

/**
 * Class name	IOrderTakerService
 * Date			30/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerService {
	/**
	 * Add a {@link CommandDTO} to the system
	 * @param commandToAdd command to add
	 * @return the traited command
	 * @throws UnknowUserException if the transmitter doesn't exist
	 * @throws EmptyDeliveryAddressException if the delivery address is empty
	 */
	CommandDTO addCommand(CommandDTO commandToAdd) throws UnknowUserException, EmptyDeliveryAddressException;
}
