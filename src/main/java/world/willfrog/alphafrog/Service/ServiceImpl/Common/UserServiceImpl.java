package world.willfrog.alphafrog.Service.ServiceImpl.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.JwtUtils;
import world.willfrog.alphafrog.Dao.Common.UserDao;
import world.willfrog.alphafrog.Entity.Common.User;
import world.willfrog.alphafrog.Service.Common.JwtService;
import world.willfrog.alphafrog.Service.Common.UserService;

import java.util.concurrent.TimeUnit;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final JwtService jwtService;

    private final StringRedisTemplate stringRedisTemplate;

    public UserServiceImpl(UserDao userDao, JwtService jwtService, StringRedisTemplate stringRedisTemplate) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

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

            // 获取今天的bitmap key
            String today = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24*60*60*1000));
            
            // 检查bitmap是否存在,不存在则创建
            boolean exists = Boolean.TRUE.equals(stringRedisTemplate.hasKey(today));
            if (!exists) {
                stringRedisTemplate.opsForValue().set(today, "");
                // 设置过期时间为2天,确保第二天还能查到昨天的登录状态
                stringRedisTemplate.expire(today, 2, TimeUnit.DAYS);
            }
            
            // 检查用户是否已登录
            Boolean isLoggedIn = stringRedisTemplate.opsForValue().getBit(today, user.getUserId());
            if (Boolean.TRUE.equals(isLoggedIn)) {
                return "ERR*User already logged in";
            }

            System.out.println("User found: " + user);

            if (user.getPassword().equals(password)) {
                stringRedisTemplate.opsForValue().setBit(today, user.getUserId(), true);

                Map<String, Object> info = new HashMap<>();
                info.put("userType", user.getUserType());
                return jwtService.generateAndSaveToken(user.getUserId().toString(), 30 * 60 * 1000);
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
    public int logout(String token){
        try {
            Map<String, Object> map = JwtUtils.extractUserId(token);

            String userID = (String) map.get("userID");
            int status = (int) map.get("status");
            if (userID == null) {
                return 0; // 直接返回登出成功
            }

            if (status != 0){
                // 防止定时任务没有处理过期的token
                String today = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24*60*60*1000));
                log.info("Changing login bit for user ID :{} in key {}", userID, today);
                stringRedisTemplate.opsForValue().setBit(today, Long.parseLong(userID), false);
                return 0;
            }
            String today = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24*60*60*1000));
            stringRedisTemplate.opsForValue().setBit(today, Long.parseLong(userID), false);
            // 删除token
            stringRedisTemplate.delete("token:" + token);

            return 0;
        } catch (Exception e) {
            log.error("Error occurred while logging out: ");
            log.error("Full Stack Trace", e);
            return 1;
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
