package world.willfrog.alphafrog.Service.ServiceImpl.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.JwtUtils;
import world.willfrog.alphafrog.Dao.Common.UserDao;
import world.willfrog.alphafrog.Entity.Common.User;
import world.willfrog.alphafrog.Service.Common.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int checkNewUsername(String username) {
        if (userDao.getUserByUsername(username) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int checkNewEmail(String email) {
        if (userDao.getUserByEmail(email) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public User createUser(String username, String password, String email, long registerTime) {
        User user = new User(null, username, password, email, registerTime, 1, 0, 100);
        userDao.createUser(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        try {
            User user = userDao.getUserByUsername(username);
            if(user == null){
                return "ERR*User not found";
            }

            System.out.println("User found: " + user);

            if (user.getPassword().equals(password)) {
                Map<String, Object> info = new HashMap<>();
                info.put("userType", user.getUserType());
                return JwtUtils.sign(user.getUserId().toString(), info);
            } else {
                return "ERR*Incorrect password";
            }
        } catch (Exception e) {
            log.error("Error occurred while logging in: ");
            log.error("Full Stack Trace", e);
            return "ERR*";
        }
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

}
