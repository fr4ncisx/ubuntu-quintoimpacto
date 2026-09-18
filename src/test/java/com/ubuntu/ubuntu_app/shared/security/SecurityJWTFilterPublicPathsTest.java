package com.ubuntu.ubuntu_app.shared.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityJWTFilterPublicPathsTest {

    @Test
    void legacyPublicEndpointsArePublic() {
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/countries"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/provinces"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/chatbot/answers"));
    }

    @Test
    void v1PublicEndpointsArePublic() {
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/countries"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/provinces"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/auth/login"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/auth/refresh"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/auth/logout"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/categories"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/publications"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/publications/5"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/publications/5/views"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/microbusiness/search"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/microbusiness"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/contact-requests"));
        assertTrue(SecurityJWTFilter.isPublicUri("/api/v1/chatbot/answers"));
        assertTrue(SecurityJWTFilter.isPublicUri("/v3/api-docs"));
        assertTrue(SecurityJWTFilter.isPublicUri("/actuator/health"));
        assertTrue(SecurityJWTFilter.isPublicUri("/actuator/health/liveness"));
        assertTrue(SecurityJWTFilter.isPublicUri("/actuator/health/readiness"));
        assertTrue(SecurityJWTFilter.isPublicUri("/actuator/prometheus"));
        assertTrue(SecurityJWTFilter.isPublicUri("/actuator/info"));
        assertTrue(SecurityJWTFilter.isPublicUri("/v3/api-docs/swagger-config"));
    }

    @Test
    void protectedEndpointsAreNotPublic() {
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/cloudinary/images"));
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/users"));
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/users/7"));
    }

    @Test
    void prefixBoundaryIsNotBypassable() {
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/microbusinessXYZ"));
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/chatbotXYZ"));
        assertFalse(SecurityJWTFilter.isPublicUri("/api/v1/categoriesXYZ"));
        assertFalse(SecurityJWTFilter.isPublicUri(null));
        assertFalse(SecurityJWTFilter.isPublicUri(""));
    }
}
