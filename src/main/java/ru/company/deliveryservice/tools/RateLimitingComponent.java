package ru.company.deliveryservice.tools;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
public class RateLimitingComponent {
    private final ConcurrentHashMap<String, RateLimiter> activeLimitersMap = new ConcurrentHashMap<>();

    @Around("@annotation(LimitedEndpoint)")
    public Object applyRateLimit(ProceedingJoinPoint joinPoint) throws Throwable{
        String endpointName = joinPoint.getSignature().toShortString();
        RateLimiter rateLimiter = activeLimitersMap.computeIfAbsent(endpointName, k -> RateLimiter.create(10.0));

        return rateLimiter.tryAcquire() ? joinPoint.proceed() : new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
    }
}
