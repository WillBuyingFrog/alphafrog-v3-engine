package world.willfrog.alphafrog.Service.Common;

import world.willfrog.alphafrog.Entity.Common.User;


public interface UserService {

    User createUser(String username, String password, String email, long registerTime);

    int login(String username, String password);

    User getUserById(String userId);

    User getUserByUsername(String username);
}
