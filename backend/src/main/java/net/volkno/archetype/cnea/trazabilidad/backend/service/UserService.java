package net.volkno.archetype.cnea.trazabilidad.backend.service;

import java.text.MessageFormat;

import net.volkno.archetype.cnea.trazabilidad.backend.model.User;
import net.volkno.archetype.cnea.trazabilidad.backend.repository.UserRepository;
import net.volkno.archetype.cnea.trazabilidad.backend.service.exception.AuthenticationException;
import net.volkno.archetype.cnea.trazabilidad.backend.service.exception.ForbiddenException;
import net.volkno.archetype.cnea.trazabilidad.backend.service.security.AuthenticatorService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Adrian Paredes
 *
 */
@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class );
	
	@Autowired
	private UserRepository userDAO;
    
    private AuthenticatorService authenticatorService;
    
	@Transactional
	public User addUser(User newUser) {
	    
		newUser = userDAO.save(newUser);
        
        LOGGER.info("User added: " + newUser.getFirstName() + " " + newUser.getLastName());
        return newUser;
	}
	
	@Transactional
    public User updateUser(User user) {
		user = userDAO.save(user);
        return user;
    }

    public User findByNick(String nick) {
        User user = userDAO.findByUsername(nick);
        return user;
    }
    
    public User findByNtUser(String ntUser) {
        User user = userDAO.findByNtUser(ntUser);
        return user;
    }
    
    public User login(String nick, String password) {

        LOGGER.info(MessageFormat.format("--- Login: {0} ---", nick));

        if (!authenticatorService.authenticate(nick, password)) {
            throw new AuthenticationException();
        }
        
        User user = findByNtUser(nick);
        if (user == null) {
            //throw new AuthenticationException("User does not exists!");
            throw new ForbiddenException();
        }
        return user;
    }
    
    @Transactional
    public User register(User newUser) {
    	newUser.setNtUser(newUser.getUsername());
        User createdUser = addUser(newUser);
        return createdUser;
    }
	
    public AuthenticatorService getAuthenticatorService() {
		return authenticatorService;
	}
    
    public void setAuthenticatorService(AuthenticatorService authenticatorService) {
		this.authenticatorService = authenticatorService;
	}
    
}