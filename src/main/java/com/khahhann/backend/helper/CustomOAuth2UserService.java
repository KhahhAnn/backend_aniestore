package com.khahhann.backend.helper;

import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Lấy thông tin từ Google
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        // Kiểm tra hoặc tạo user trong database
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            user = new Users();
            user.setEmail(email);
            user.setFirstName(name);
            user.setImageSrc(picture);
            user.setPassword(""); // Không cần password
            user.setActive(true);
            userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2User, user);
    }
}