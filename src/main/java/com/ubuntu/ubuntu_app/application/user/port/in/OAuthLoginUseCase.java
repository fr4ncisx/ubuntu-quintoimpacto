package com.ubuntu.ubuntu_app.application.user.port.in;

import com.ubuntu.ubuntu_app.application.user.api.GoogleOidcProfile;

public interface OAuthLoginUseCase {

    OAuthLoginResult login(GoogleOidcProfile profile, int expirationTime);
}
