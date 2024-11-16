package world.willfrog.alphafrog.ScheduledTask;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartupTask {
    
    private final StringRedisTemplate stringRedisTemplate;

    public AppStartupTask(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeBitmapOnStartup() {
        try{
            String todayKey = String.format("user_login_bitmap:%d", System.currentTimeMillis() / (24*60*60*1000));
            Boolean exists = Boolean.TRUE.equals(stringRedisTemplate.hasKey(todayKey));
            if (!exists) {
                String yesterdayKey = String.format("user_login_bitmap:%d", (System.currentTimeMillis() - 24*60*60*1000) / (24*60*60*1000));
                String yesterdayValue = stringRedisTemplate.opsForValue().get(yesterdayKey);
                if (yesterdayValue != null) {
                    stringRedisTemplate.opsForValue().set(todayKey, yesterdayValue);
                } else {
                    stringRedisTemplate.opsForValue().set(todayKey, "");
                }
                stringRedisTemplate.expire(todayKey, 2, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("Error occurred while initializing bitmap on startup", e);
        }
        
    }

}
