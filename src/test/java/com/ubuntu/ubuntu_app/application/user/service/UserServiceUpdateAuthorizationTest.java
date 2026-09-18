package com.ubuntu.ubuntu_app.application.user.service;

import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import com.ubuntu.ubuntu_app.infrastructure.user.adapter.mapper.UserMapper;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceUpdateAuthorizationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private UpdateUserRequest updateDto() {
        return new UpdateUserRequest("Nombre", "Apellido", "123", "http://img", true);
    }

    private UserEntity user(String email, UserRole role) {
        return new UserEntity("Nombre", "Apellido", email, role, "123", null);
    }

    private void authenticateAs(UserEntity principal) {
        var authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void userCanUpdateOwnAccount() {
        var me = user("me@mail.com", UserRole.USER);
        authenticateAs(me);
        Mockito.when(userRepository.findByEmail("me@mail.com")).thenReturn(Optional.of(me));

        assertDoesNotThrow(() -> userService.update(updateDto(), "me@mail.com"));
    }

    @Test
    void userCannotUpdateAnotherAccount() {
        authenticateAs(user("me@mail.com", UserRole.USER));

        assertThrows(AccessDeniedException.class,
                () -> userService.update(updateDto(), "victim@mail.com"));
        Mockito.verify(userRepository, Mockito.never()).findByEmail(Mockito.anyString());
    }

    @Test
    void adminCanUpdateAnotherAccount() {
        var victim = user("victim@mail.com", UserRole.USER);
        authenticateAs(user("admin@mail.com", UserRole.ADMIN));
        Mockito.when(userRepository.findByEmail("victim@mail.com")).thenReturn(Optional.of(victim));

        assertDoesNotThrow(() -> userService.update(updateDto(), "victim@mail.com"));
    }

    @Test
    void unauthenticatedUpdateIsDenied() {
        assertThrows(AccessDeniedException.class,
                () -> userService.update(updateDto(), "victim@mail.com"));
    }

    @Test
    void adminCanDeactivateUser() {
        var victim = user("victim@mail.com", UserRole.USER);
        authenticateAs(user("admin@mail.com", UserRole.ADMIN));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(victim));

        assertDoesNotThrow(() -> userService.deactivate(2L));
        assertFalse(victim.isActive());
        Mockito.verify(userRepository).save(victim);
    }

    @Test
    void userCannotDeactivateUser() {
        authenticateAs(user("user@mail.com", UserRole.USER));

        assertThrows(AccessDeniedException.class,
                () -> userService.deactivate(1L));
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyLong());
    }

    @Test
    void unauthenticatedDeactivateIsDenied() {
        assertThrows(AccessDeniedException.class,
                () -> userService.deactivate(1L));
    }
}
