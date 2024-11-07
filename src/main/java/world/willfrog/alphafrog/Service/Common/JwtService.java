package world.willfrog.alphafrog.Service.Common;

import java.util.Map;

public interface JwtService {

    String generateAndSaveToken(String userId, long expireTime);
}
