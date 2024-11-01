package world.willfrog.alphafrog.Common;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtils {

    private static final long EXPIRE_TIME = 30 * 60 * 1000;


    // private static final String SECRET = "OMGAlphaFrog1124727";


    public static String sign(String userId, Map<String, Object> info) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

            SecretKey key = Jwts.SIG.HS256.key().build();

            return Jwts.builder()
                    .subject(userId).claim("info", info.toString())
                    .expiration(date)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            log.error("Error occurred while signing new token for user #" + userId);
            log.error("Error Stack Trace:", e);
            return null;
        }
    }

    public static boolean checkSign(String token, String userId, Map<String, Object> info) {
        try {

            SecretKey key = Jwts.SIG.HS256.key().build();
            var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            assert claims.getSubject().equals(userId);
            assert claims.get("info").equals(info.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
