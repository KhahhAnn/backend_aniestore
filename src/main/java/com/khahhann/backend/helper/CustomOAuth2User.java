package com.khahhann.backend.helper;


import com.khahhann.backend.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final Users user;

    public CustomOAuth2User(OAuth2User oAuth2User, Users user) {
        super(user.getEmail(), "", oAuth2User.getAuthorities());
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return user.getFirstName();
    }

    public Users getUser() {
        return user;
    }
}
