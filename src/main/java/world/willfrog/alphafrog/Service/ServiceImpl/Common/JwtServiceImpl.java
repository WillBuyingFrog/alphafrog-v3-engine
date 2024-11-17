package world.willfrog.alphafrog.Service.ServiceImpl.Common;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.JwtUtils;
import world.willfrog.alphafrog.Service.Common.JwtService;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtServiceImpl implements JwtService {

    private final StringRedisTemplate stringRedisTemplate;

    public JwtServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String generateAndSaveToken(String userId, long expireTime) {

        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = JwtUtils.sign(userId, expireDate);
        // token过期之后由定时任务清除，因此设置token在redis中的过期时间为用户端token过期时间的
        // 2倍，这样保证过期之后定时任务能扫描到，并且可以由定时任务设置bitmap
        stringRedisTemplate.opsForValue().set("token:" + token, userId, expireTime * 2, TimeUnit.MILLISECONDS);
        return token;
    }
}
