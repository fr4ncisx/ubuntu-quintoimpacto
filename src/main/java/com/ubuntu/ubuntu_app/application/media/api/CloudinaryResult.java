package com.ubuntu.ubuntu_app.application.media.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CloudinaryResult(
        @JsonAlias("result") String result) {
}
