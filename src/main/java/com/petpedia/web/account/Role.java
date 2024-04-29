package com.petpedia.web.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "user"),
    ADMIN("ROLE_ADMIN", "admin"),
    ORG("ROLE_ORG", "organization");

    private final String key;
    private final String title;
}
