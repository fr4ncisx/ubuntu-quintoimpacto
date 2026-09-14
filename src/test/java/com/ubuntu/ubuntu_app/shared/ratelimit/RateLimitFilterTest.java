package com.ubuntu.ubuntu_app.shared.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitFilterTest {

    private MockHttpServletRequest request(String uri, String ip) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", uri);
        request.setRemoteAddr(ip);
        return request;
    }

    @Test
    void allowsUnderLimit() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();

        for (int i = 0; i < 10; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request("/contact/new-request", "1.2.3.4"), response, new MockFilterChain());
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    void blocksOverLimitWithRetryAfter() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        MockHttpServletResponse last = null;
        for (int i = 0; i < 11; i++) {
            last = new MockHttpServletResponse();
            filter.doFilter(request("/contact/new-request", "5.6.7.8"), last, new MockFilterChain());
        }

        assertNotNull(last);
        assertEquals(429, last.getStatus());
        assertEquals("60", last.getHeader("Retry-After"));
    }

    @Test
    void bucketsArePerClientIp() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        for (int i = 0; i < 10; i++) {
            filter.doFilter(request("/contact/new-request", "9.9.9.9"),
                    new MockHttpServletResponse(), new MockFilterChain());
        }
        MockHttpServletResponse other = new MockHttpServletResponse();
        filter.doFilter(request("/contact/new-request", "8.8.8.8"), other, new MockFilterChain());

        assertEquals(200, other.getStatus());
    }

    @Test
    void unlistedPathsAreUnlimited() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        for (int i = 0; i < 50; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request("/api/v1/users", "1.1.1.1"), response, new MockFilterChain());
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    void forwardedHeaderIsUsedAsClientIp() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        for (int i = 0; i < 10; i++) {
            MockHttpServletRequest request = request("/contact/new-request", "10.0.0.1");
            request.addHeader("X-Forwarded-For", "7.7.7.7, 10.0.0.1");
            filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
        }
        MockHttpServletRequest blocked = request("/contact/new-request", "10.0.0.1");
        blocked.addHeader("X-Forwarded-For", "7.7.7.7, 10.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(blocked, response, new MockFilterChain());

        assertEquals(429, response.getStatus());
    }
}
