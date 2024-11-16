package world.willfrog.alphafrog.Common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {

    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    private static final String SECRET = "OMGAlphaFrog1124727OMGAlphaFrog1124727OMGAlphaFrog1124727OMGAlphaFrog1124727";

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String sign(String userId, Date expireTime) {
        try {
            return Jwts.builder()
                    .subject(userId)
                    .expiration(expireTime)
                    .signWith(KEY)
                    .compact();

        } catch (Exception e) {
            log.error("Error occurred while signing new token for user #{}", userId);
            log.error("Error Stack Trace:", e);
            return null;
        }
    }

    public static boolean checkSign(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
            Date expiration = claims.getExpiration();
//            System.out.println("Expiration: " + expiration);
//            System.out.println("Current date: " + new Date());
            if (expiration != null && expiration.before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
//            log.error("Error Stack Trace:", e);
            return false;
        }
    }

    public static Map<String, Object> extractUserId(String token) {
        Map<String, Object> ret = new HashMap<>();
        try {
            Claims claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
            ret.put("status", 0);
            ret.put("userID", claims.getSubject());
            return ret;
        } catch (ExpiredJwtException e) {
            log.info("Expired token found!");
            Claims claims = e.getClaims();
            ret.put("status", -1);
            String userId = claims.getSubject();
            ret.put("userID", userId);
            log.info("corresponding user ID: " + userId);
            return ret;
        } catch (Exception e) {
            log.error("Error occurred while extracting user ID from token. Token might have expired");
            ret.put("status", -2);
            ret.put("userID", null);
            return ret;
        }
    }




}
