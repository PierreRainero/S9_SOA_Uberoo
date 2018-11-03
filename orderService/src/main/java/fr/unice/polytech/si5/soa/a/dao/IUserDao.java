package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.User;

/**
 * Class name	IUserDao
 * Date			30/09/2018
 * @author		PierreRainero
 * 
 * @version		1.2
 * Date			03/11/2018
 */
public interface IUserDao {
	/**
	 * Add an {@link userToAdd} into the database
	 * @param userToAdd user to add
	 * @return the saved user
	 */
	User addUser(User userToAdd);
	
	/**
	 * Search an user in the database using his id
	 * @param userId id to search
	 * @return the user wrapped in an {@link Optional} if the user exists, Optional.empty() otherwise
	 */
	Optional<User> findUserById(int userId);
}
