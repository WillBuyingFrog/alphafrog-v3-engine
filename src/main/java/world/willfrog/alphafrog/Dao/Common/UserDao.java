package world.willfrog.alphafrog.Dao.Common;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import world.willfrog.alphafrog.Entity.Common.User;

@Mapper
public interface UserDao {

    @Insert("INSERT INTO alphafrog_user (username, password, email, register_time, user_type, user_level, credit) " +
            "VALUES (#{username}, #{password}, #{email}, #{registerTime}, #{userType}, #{userLevel}, #{credit})")
    int createUser(User user);


    @Select("SELECT * FROM alphafrog_user WHERE user_id = #{userId}")
    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "user_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "username", column = "username", jdbcType = JdbcType.VARCHAR),
            @Result(property = "password", column = "password", jdbcType = JdbcType.VARCHAR),
            @Result(property = "email", column = "email", jdbcType = JdbcType.VARCHAR),
            @Result(property = "registerTime", column = "register_time", jdbcType = JdbcType.BIGINT),
            @Result(property = "userType", column = "user_type", jdbcType = JdbcType.INTEGER),
            @Result(property = "userLevel", column = "user_level", jdbcType = JdbcType.INTEGER),
            @Result(property = "credit", column = "credit", jdbcType = JdbcType.INTEGER)
    })
    User getUserById(String userId);

    @Select("SELECT * FROM alphafrog_user WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM alphafrog_user WHERE email like #{email}")
    User getUserByEmail(String email);
}
