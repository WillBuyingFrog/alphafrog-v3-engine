package world.willfrog.alphafrog.ScheduledTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import world.willfrog.alphafrog.Common.JwtUtils;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UserScheduledTask {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public UserScheduledTask(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * 定时任务，每5分钟执行一次，清除过期的token
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次，调试的时候改成每5秒执行一次
    public void clearExpiredTokens() {
        log.info("Cleaning expired tokens");
        Set<String> tokenKeys = stringRedisTemplate.keys("token:*");
        assert tokenKeys != null;
        if (!tokenKeys.isEmpty()) {
            for (String tokenKey : tokenKeys) {
                String token = tokenKey.substring(6); // 去掉前缀"token:"
                if (!JwtUtils.checkSign(token)) { // 检查 token 是否过期
                    // 如果过期，就拿到过期token对应的用户id，将今天bitmap的userId位置0
                    // 代表登出，然后删掉这个键值对
                    String userId = stringRedisTemplate.opsForValue().get(tokenKey);
                    log.info("Token for userId {} expired: {}", userId, token);
                    if (userId != null) {
                        String today = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24 * 60 * 60 * 1000));
                        stringRedisTemplate.opsForValue().setBit(today, Long.parseLong(userId), false);
                        stringRedisTemplate.delete(tokenKey);
                    }
                }
            }
        }
    }

    /**
     * 定时任务，每天午夜执行，创建新的用户登录bitmap
     */
    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜运行
    public void createNewDayBitmap() {
        String yesterdayKey = String.format("user_login_bitmap:%d", (System.currentTimeMillis() / (24*60*60*1000)) - 1);
        String todayKey = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24*60*60*1000));

        boolean exists = Boolean.TRUE.equals(stringRedisTemplate.hasKey(todayKey));
        if (!exists) {
            String bitmapData = stringRedisTemplate.opsForValue().get(yesterdayKey);
            stringRedisTemplate.opsForValue().set(todayKey, Objects.requireNonNullElse(bitmapData, ""));
            stringRedisTemplate.expire(todayKey, 2, TimeUnit.DAYS);
            clearExpiredTokens(); // 再执行一遍清除过期 token 任务
        }
    }
}
