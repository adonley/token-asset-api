package io.block16.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private int MINUTE_LIMIT = 30;
    private final long ONE_MINUTE = 60 * 1000;
    private final ValueOperations<String, Object> valueOperations;
    private static String TOO_MAN_REQUESTS_JSON;

    public RateLimiterInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
        GenericResponse<Object> genericResponse = new GenericResponse<>(null);
        genericResponse.setMessage("Too many requests. Please wait until next period to send additional requests.");
        genericResponse.setError(true);
        try {
            TOO_MAN_REQUESTS_JSON = new ObjectMapper().writeValueAsString(genericResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Don't rate limit options requests. Hope we don't come to regret this
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // Get the IP address from the incoming headers
        String ipAddress = this.getClientIpAddress(request);
        Object cachedInfoObj = this.valueOperations.get(ipAddress);
        RateLimitDTO rateLimitDTO;

        // If we don't have related object, create a new one
        if (cachedInfoObj == null) {
            rateLimitDTO = new RateLimitDTO();
            rateLimitDTO.setBanned(false);
            rateLimitDTO.setRequestCount(1);
            rateLimitDTO.setTimestamp(Instant.now().toEpochMilli());

            // Expire after a minute
            this.valueOperations.set(ipAddress, rateLimitDTO, 1, TimeUnit.MINUTES);
            response.addHeader("X-RateLimit-Remaining", String.valueOf(MINUTE_LIMIT - rateLimitDTO.getRequestCount()));
            response.addHeader("X-RateLimit-Limit", String.valueOf(MINUTE_LIMIT));
            response.addHeader("X-RateLimit-Reset", String.valueOf(rateLimitDTO.getTimestamp() + ONE_MINUTE));
            return true;
        }

        rateLimitDTO = (RateLimitDTO) cachedInfoObj;

        // Request count
        rateLimitDTO.setRequestCount(rateLimitDTO.getRequestCount() + 1);
        long timeRemaining = rateLimitDTO.getTimestamp() + ONE_MINUTE - Instant.now().toEpochMilli();
        this.valueOperations.set(ipAddress, rateLimitDTO, Math.max(timeRemaining, 0), TimeUnit.MILLISECONDS);
        int remainingRequests = Math.max(MINUTE_LIMIT - rateLimitDTO.getRequestCount(), 0);
        response.addHeader("X-RateLimit-Remaining", String.valueOf(remainingRequests));
        response.addHeader("X-RateLimit-Limit", String.valueOf(MINUTE_LIMIT));
        response.addHeader("X-RateLimit-Reset", String.valueOf(Instant.now().toEpochMilli() + timeRemaining));

        // Should allow
        boolean allowRequest = rateLimitDTO.getRequestCount() <= MINUTE_LIMIT;

        if(!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.print(TOO_MAN_REQUESTS_JSON);
            writer.flush();
            return false;
        }
        return true;
    }
}
