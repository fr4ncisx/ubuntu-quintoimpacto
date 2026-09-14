package com.ubuntu.ubuntu_app.application.user.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.application.media.service.CloudinaryService;
import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;
import com.ubuntu.ubuntu_app.application.user.api.GoogleUserProfile;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginResult;
import com.ubuntu.ubuntu_app.application.user.port.in.OAuthLoginUseCase;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.config.TokenProperties;
import com.ubuntu.ubuntu_app.shared.security.JWTUtils;
import com.ubuntu.ubuntu_app.shared.support.LastNameGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthLoginService implements OAuthLoginUseCase {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final JWTUtils jwtUtils;
    private final TokenProperties tokenProperties;

    @Override
    @Transactional
    public OAuthLoginResult login(GoogleOidcProfile profile, int expirationTime) {
        int validity = expirationTime > 0 ? expirationTime : tokenProperties.expiration();
        var existing = userRepository.findByEmail(profile.email());
        if (existing.isPresent()) {
            var user = existing.get();
            refreshProfilePhotoIfMissing(user, profile.picture());
            return new OAuthLoginResult(jwtUtils.generate(user, validity), false);
        }
        String lastName = profile.familyName() != null ? profile.familyName()
                : LastNameGenerator.obtainRandomName();
        String profileImg = uploadProfilePhoto(profile.picture());
        var newLocalUser = new GoogleUserProfile(profile.email(),
                Objects.requireNonNullElse(profile.givenName(), ""), lastName, profileImg);
        var userEntity = new UserEntity(newLocalUser);
        userRepository.save(userEntity);
        return new OAuthLoginResult(jwtUtils.generate(userEntity, validity), true);
    }

    private String uploadProfilePhoto(String picture) {
        if (picture == null) {
            return null;
        }
        try {
            return cloudinaryService.uploadProfilePhoto(picture, 384);
        } catch (IOException | URISyntaxException e) {
            log.warn("No se pudo subir la foto de perfil: {}", e.getMessage());
            return null;
        }
    }

    private void refreshProfilePhotoIfMissing(UserEntity user, String picture) {
        if (user.getImage() == null && picture != null) {
            try {
                log.info("Debería actualizar tu foto de perfil a Cloudinary");
                user.setImage(cloudinaryService.uploadProfilePhoto(picture, 384));
            } catch (IOException | URISyntaxException e) {
                log.warn("No se pudo actualizar la foto de perfil: {}", e.getMessage());
            }
        }
    }
}
