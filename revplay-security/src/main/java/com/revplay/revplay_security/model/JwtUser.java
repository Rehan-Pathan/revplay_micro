package com.revplay.revplay_security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JwtUser {

    private Long userId;
    private Long artistId;
    private String email;
    private List<String> roles;

}
