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
    public String generateAndSaveToken(String userId, Map<String, Object> info, long expireTime) {

        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = JwtUtils.sign(userId, info, expireDate);
        stringRedisTemplate.opsForValue().set("token:" + token, userId, expireTime, TimeUnit.MILLISECONDS);
        return token;
    }
}
