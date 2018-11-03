package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.UserDTO;

/**
 * Class name	IUserService
 * Date			03/11/2018
 * @author		PierreRainero
 *
 */
public interface IUserService {
	/**
	 * Add a {@link UserDTO} to the system
	 * @param user user to add
	 * @return the treated user
	 */
	UserDTO addUser(UserDTO user);
}
