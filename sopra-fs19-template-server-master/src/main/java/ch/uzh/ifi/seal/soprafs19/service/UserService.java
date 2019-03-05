package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserByUsername(String username) { return this.userRepository.findByUsername(username); }

    public User loginUser(String username, String password) {
        User temp = this.userRepository.findByUsername(username);
        if (temp == null) throw new UserNotFoundException(username);
        if (temp.getPassword().equals(password)) {
            temp.setStatus(UserStatus.ONLINE);
            return temp;
        }
        else throw new PasswordNotValidException(username);
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
}
