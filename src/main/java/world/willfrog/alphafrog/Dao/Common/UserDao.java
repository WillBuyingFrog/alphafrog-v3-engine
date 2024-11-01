package world.willfrog.alphafrog.Dao.Common;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import world.willfrog.alphafrog.Entity.Common.User;

@Mapper
public interface UserDao {

    @Insert("INSERT INTO user (user_id, username, password, email, phone, created_at, updated_at) " +
            "VALUES (#{userId}, #{username}, #{password}, #{email}, #{phone}, #{createdAt}, #{updatedAt})")
    int createUser(User user);

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User getUserById(String userId);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);
}
