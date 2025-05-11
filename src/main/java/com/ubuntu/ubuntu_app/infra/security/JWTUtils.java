package com.ubuntu.ubuntu_app.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ubuntu.ubuntu_app.Repository.UserRepository;
import com.ubuntu.ubuntu_app.model.dto.UserGoogleDTO;
import com.ubuntu.ubuntu_app.model.entities.UserEntity;
import com.ubuntu.ubuntu_app.model.generator.LastNameGenerator;
import com.ubuntu.ubuntu_app.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Value("${jwt.secret.key}")
    private String secret;

    public String generateToken(UserEntity user, int expirationTime) {
		return JWT.create()
            .withIssuer("Ubuntu Application")
            .withSubject(user.getEmail())
            .withIssuedAt(getCurrentTime())
            .withExpiresAt(expirationTime(expirationTime))            
            .withClaim("nombre", user.getNombre())
            .withClaim("apellido", user.getApellido())
            .withClaim("telefono", user.getTelefono())
            .withClaim("rol", user.getRol().name())
            .withClaim("imagen", user.getImagen()) 
            .withClaim("newsletter", user.getSuscribed())   
            .withClaim("vencimiento", LocalDateTime.ofInstant(expirationTime(expirationTime), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .withJWTId(UUID.randomUUID().toString()).sign(getAlgorithm());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }

    private Instant getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    }

    private Instant expirationTime(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant();
    }

    public String validateTokenLocal(String token) {
        JWTVerifier verifier = 
            JWT.require(getAlgorithm())
            .withIssuer("Ubuntu Application")
            .build();
        return verifier.verify(token).getSubject();
    }

    @Transactional
    public String createLocalAccount(Payload payload, int expirationTime) throws IOException, URISyntaxException {
        String email = payload.getEmail();
        String given_name = payload.get("given_name").toString();
        Object family_name = payload.get("family_name");
        Object profile_img = payload.get("picture");
        var checkIfUserExists = userFinder(email);
        if (checkIfUserExists != null) {
            return null;
        }
        UserGoogleDTO newLocalUser;
        if (family_name != null) {
            newLocalUser = new UserGoogleDTO(email, given_name, family_name.toString(), null);
        } else {
            newLocalUser = new UserGoogleDTO(email, given_name, LastNameGenerator.obtainRandomName(), null);
        }
        var cloudinaryURL = cloudinaryService.profilePhotoUploader(profile_img, 384);
        newLocalUser.setProfileImg(cloudinaryURL);
        UserEntity userEntity = new UserEntity(newLocalUser);
        userRepository.save(userEntity);
        return generateToken(userEntity, expirationTime);
    }

    public Payload extractGooglePayload(String googleToken) {
        HttpTransport transport = new NetHttpTransport();
        GsonFactory gsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(transport, gsonFactory);
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(googleToken);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (GeneralSecurityException | IOException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    /**
     * User finder by email
     * @param email
     * @return UserEntity if is present
     */
    public UserEntity userFinder(String email) {
        var user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Update profile img if doesnt have one in database by using google payload data
     * @param email
     * @param payload
     * @return UserEntity
     * @throws URISyntaxException 
     * @throws IOException 
     */
    @Transactional()
    public UserEntity userFinder(String email, Payload payload) {
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            var userExists = user.get();
            if (userExists.getImagen() == null) {
                try {
                    log.info("Debería actualizar tu foto de perfil a Cloudinary");
                    var cloudinaryURL = cloudinaryService.profilePhotoUploader(payload.get("picture"), 384);
                    userExists.setImagen(cloudinaryURL);
                } catch (IOException | URISyntaxException e) {
                    return null;
                }
            }
            return userExists;
        }
        return null;
    }

    public boolean validateToken(String token, UserEntity user) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new NetHttpTransport(), new GsonFactory());
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                if (user != null) {
                    String email = payload.getEmail();
                    return email.equalsIgnoreCase(user.getEmail());
                }
            } else {
                try {
                    Algorithm algorithm = getAlgorithm();
                    if (user != null) {
                        if (user.getEmail() != null) {
                            JWTVerifier jwt_verifier = JWT.require(algorithm)
                                    .withSubject(user.getEmail())
                                    .build();
                            jwt_verifier.verify(token);
                            return true;
                        } else {
                            return false;
                        }
                    }
                } catch (JWTVerificationException ex) {
                    return false;
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            log.warn(e.getMessage());
        }
        return false;
    }
}