package world.willfrog.alphafrog.Service.ServiceImpl.Common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Dao.Common.UserDao;
import world.willfrog.alphafrog.Entity.Common.User;
import world.willfrog.alphafrog.Service.Common.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User createUser(String username, String password, String email, long registerTime) {
        User user = new User(null, username, password, email, registerTime, 1, 0, 100);
        userDao.createUser(user);
        return user;
    }

    @Override
    public int login(String username, String password) {
        try {
            User user = userDao.getUserByUsername(username);
            if (user.getPassword().equals(password)) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
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
