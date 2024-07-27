package com.ubuntu.ubuntu_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGoogleDTO {
    private String email;
    private String name;
    private String lastName;
}
