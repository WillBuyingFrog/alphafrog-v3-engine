package world.willfrog.alphafrog.Service.Common;

import world.willfrog.alphafrog.Entity.Common.User;


public interface UserService {

    int checkNewUsername(String username);

    int checkNewEmail(String email);

    User createUser(String username, String password, String email, long registerTime);

    String login(String username, String password);

    int logout(String token);

    User getUserById(String userId);

    User getUserByUsername(String username);
}
