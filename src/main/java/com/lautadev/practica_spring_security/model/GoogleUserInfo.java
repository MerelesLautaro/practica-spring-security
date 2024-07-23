package com.lautadev.practica_spring_security.model;

import java.util.Map;

public class GoogleUserInfo {

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("given_name");
    }

    public String getLastname() {
        return (String) attributes.get("family_name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }
}
