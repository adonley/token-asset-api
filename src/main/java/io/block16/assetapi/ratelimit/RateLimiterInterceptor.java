/*

package io.block16.assetapi.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${rate.limit.enabled:true}")
    private boolean enabled;
    @Value("${rate.limit.minute:20}")
    private int minuteLimit;

    @Autowired
    public RateLimiterInterceptor() {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean allowRequest = true;

        if (!enabled) {
            return true;
        }

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        response.addHeader("X-RateLimit-Limit", String.valueOf(minuteLimit));

        return allowRequest;
    }

    @PreDestroy
    public void destroy() {
        // loop and finalize all limiters
    }
}

*/
