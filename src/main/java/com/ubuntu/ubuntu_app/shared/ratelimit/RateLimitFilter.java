package com.ubuntu.ubuntu_app.shared.ratelimit;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private static final List<Rule> RULES = List.of(
            new Rule("/contact/new-request", 10),
            new Rule("/api/v1/contact-requests", 10),
            new Rule("/api/v1/auth/login", 10),
            new Rule("/chatbot", 30),
            new Rule("/api/v1/chatbot", 30),
            new Rule("/oauth2/", 20),
            new Rule("/login/oauth2/", 20),
            new Rule("/publications/click", 60),
            new Rule("/api/v1/publications/", 120));

    private final boolean trustForwardedHeader;

    public RateLimitFilter() {
        this(false);
    }

    public RateLimitFilter(
            @org.springframework.beans.factory.annotation.Value("${app.security.rate-limit.trust-forwarded-headers:false}") boolean trustForwardedHeader) {
        this.trustForwardedHeader = trustForwardedHeader;
    }

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, java.io.IOException {
        String uri = request.getRequestURI();
        Rule rule = match(uri);
        if (rule != null) {
            Bucket bucket = buckets.get(clientKey(request, rule), key -> newBucket(rule.perMinute()));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded: ip={} uri={}", clientKey(request, rule), uri);
                response.setStatus(429);
                response.setHeader("Retry-After", "60");
                response.setHeader("Content-Type", "application/json");
                response.getWriter().write("{\"Error\": \"Demasiadas solicitudes, intenta en un minuto\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Rule match(String uri) {
        if (uri == null) {
            return null;
        }
        for (Rule rule : RULES) {
            String boundary = rule.prefix().endsWith("/") ? rule.prefix() : rule.prefix() + "/";
            if (uri.equals(rule.prefix()) || uri.startsWith(boundary)) {
                return rule;
            }
        }
        return null;
    }

    private String clientKey(HttpServletRequest request, Rule rule) {
        String remoteAddr = request.getRemoteAddr();
        String ip = remoteAddr;
        if (trustForwardedHeader) {
            String forwarded = request.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                ip = forwarded.split(",")[0].trim();
            }
        }
        return rule.prefix() + "|" + ip;
    }

    private Bucket newBucket(long perMinute) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(perMinute, Refill.intervally(perMinute, Duration.ofMinutes(1))))
                .build();
    }

    private record Rule(String prefix, long perMinute) {
    }
}
