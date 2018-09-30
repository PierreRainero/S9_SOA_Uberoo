package fr.unice.polytech.si5.soa.a.services.component;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Command;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;

/**
 * Class name	OrderTakerServiceImpl
 * @see 		IOrderTakerService
 * Date			30/09/2018
 * @author		PierreRainero
**/
@Primary
@Service("OrderTakerService")
public class OrderTakerServiceImpl implements IOrderTakerService {
	private static Logger logger = LogManager.getLogger(OrderTakerServiceImpl.class);
	
	@Autowired
	private IOrderTakerDao orderDao;
	
	@Autowired
	private IUserDao userDao;

	@Override
	/**
     * {@inheritDoc}
     */
	public CommandDTO addCommand(CommandDTO commandToAdd) throws UnknowUserException, EmptyDeliveryAddressException {
		Command command = new Command(commandToAdd);
		
		if(command.getDeliveryAddress()==null || command.getDeliveryAddress().isEmpty()) {
			throw new EmptyDeliveryAddressException("Delivery address cannot be empty");
		}
		
		Optional<User> userWrapped = userDao.findUserById(commandToAdd.getTransmitter().getId());
		if(!userWrapped.isPresent()) {
			throw new UnknowUserException("Can't find user with id = "+commandToAdd.getTransmitter().getId());
		}
		command.setTransmitter(userWrapped.get());
		
		// @TODO Aller chercher les plats via un dao pour instancier l'objet command correctement avant de le passer
		return orderDao.addCommand(command).toDTO();
	}
	
}