package io.block16.assetapi.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import java.util.concurrent.TimeUnit;


public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private int minuteLimit = 30;
    private final long oneMinute = 60 * 1000;
    private final ValueOperations<String, Object> valueOperations;

    public RateLimiterInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
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
            response.addHeader("X-RateLimit-Remaining", String.valueOf(minuteLimit - rateLimitDTO.getRequestCount()));
            response.addHeader("X-RateLimit-Limit", String.valueOf(minuteLimit));
            return true;
        }

        rateLimitDTO = (RateLimitDTO) cachedInfoObj;

        // Request count
        rateLimitDTO.setRequestCount(rateLimitDTO.getRequestCount() + 1);
        long timeRemaining = rateLimitDTO.getTimestamp() + oneMinute - Instant.now().toEpochMilli();
        this.valueOperations.set(ipAddress, rateLimitDTO, Math.max(timeRemaining, 0), TimeUnit.MILLISECONDS);
        int remainingRequests = Math.max(minuteLimit - rateLimitDTO.getRequestCount(), 0);
        response.addHeader("X-RateLimit-Remaining", String.valueOf(remainingRequests));
        response.addHeader("X-RateLimit-Limit", String.valueOf(minuteLimit));

        LOGGER.debug("Remaining time: {}, Request remaining: {}", timeRemaining, remainingRequests);

        // Should allow
        boolean allowRequest = rateLimitDTO.getRequestCount() <= minuteLimit;

        if(!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        return true;
    }
}
