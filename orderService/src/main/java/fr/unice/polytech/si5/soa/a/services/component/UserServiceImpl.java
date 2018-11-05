package fr.unice.polytech.si5.soa.a.services.component;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.UserDTO;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.services.IUserService;

/**
 * Class name	UserService
 *
 * @author 		PierreRainero
 * @see 		IUserService
 * Date			03/11/2018
 **/
@Primary
@Service("UserService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDao userDao;
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public UserDTO addUser(UserDTO user) {
		Optional<User> existingUser = userDao.findUserByName(user.getFirstName(), user.getLastName());
		if(existingUser.isPresent()) {
			return existingUser.get().toDTO();
		}
		
		return userDao.addUser(new User(user)).toDTO();
	}
}
