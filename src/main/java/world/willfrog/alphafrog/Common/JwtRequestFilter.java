package world.willfrog.alphafrog.Common;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.ArrayList;


@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final StringRedisTemplate stringRedisTemplate;

    public JwtRequestFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    {

//        System.out.println("Request path: " + request.getServletPath());
        try {

            final String authorizationHeader = request.getHeader("Authorization");
//            System.out.println("Authorization header: " + authorizationHeader);

            String userId;
            String token;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
//                System.out.println("Token: " + token);
                if(JwtUtils.checkSign(token)) {
//                    System.out.println("Valid token: " + token);
                    userId = stringRedisTemplate.opsForValue().get("token:" + token);
                    // Check if token is expired
                    if(userId != null){
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId,null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occurred while checking authorization: {}", e.getMessage());
            log.error("Full stack trace: ", e);
        }

    }

}
