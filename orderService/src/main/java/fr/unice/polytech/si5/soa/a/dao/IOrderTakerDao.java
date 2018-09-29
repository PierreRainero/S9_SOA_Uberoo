package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Command;

/**
 * Class name	IOrderTaker
 * Date			29/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerDao {
	/**
	 * Add a {@link Command} into the database
	 * @param commandToAdd command to add
	 * @return the saved command
	 */
	Command addCommand(Command commandToAdd);
}
