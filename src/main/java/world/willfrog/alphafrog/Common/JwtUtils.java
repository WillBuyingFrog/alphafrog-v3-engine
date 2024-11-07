package world.willfrog.alphafrog.Common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
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

            Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
            
            return true;
        } catch (Exception e) {
            // log.error("Error Stack Trace:", e);
            return false;
        }
    }

    public static String extractUserId(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error occurred while extracting user ID from token");
            log.error("Error Stack Trace:", e);
            return null;
        }
    }




}
