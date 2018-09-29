package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;

public interface IOrderTakerService {
	/**
	 * Add a {@link CommandDTO} to the system
	 * @param commandToAdd command to add
	 * @return the traited command
	 */
	CommandDTO addCommand(CommandDTO commandToAdd);
}
