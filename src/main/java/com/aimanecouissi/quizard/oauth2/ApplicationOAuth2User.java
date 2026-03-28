package com.aimanecouissi.quizard.oauth2;

import com.aimanecouissi.quizard.enums.Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ApplicationOAuth2User implements OAuth2User {
    private final OAuth2User oauth2User;
    private final String oauth2ClientName;

    public ApplicationOAuth2User(OAuth2User oauth2User, String oauth2ClientName) {
        this.oauth2ClientName = oauth2ClientName;
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(oauth2User.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("email");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getFullName() {
        return oauth2User.getAttribute("name");
    }

    public Provider getOauth2ClientName() {
        return Provider.valueOf(oauth2ClientName.toUpperCase());
    }
}