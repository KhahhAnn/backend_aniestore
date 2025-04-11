package com.khahhann.backend.controller;

import com.auth0.jwt.JWT;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.Roles;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.repository.RolesRepository;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.request.CodeExchangeRequest;
import com.khahhann.backend.request.TokenRequest;
import com.khahhann.backend.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.interfaces.DecodedJWT;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class KeycloakAuth {
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartRepository cartRepository;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final Keycloak keycloak;

    public KeycloakAuth(Keycloak keycloak, UserRepository usersRepository, RolesRepository rolesRepository) {
        this.keycloak = keycloak;
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest request) {
        try {
            // Xác thực token
            AccessToken token = verifyToken(request.getToken());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user", Map.of(
                            "username", token.getPreferredUsername(),
                            "email", token.getEmail(),
                            "roles", token.getRealmAccess().getRoles()
                    )
            ));

        } catch (VerificationException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Token không hợp lệ: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Lỗi server: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> exchangeCode(@RequestBody CodeExchangeRequest request) {
        try {
            String tokenUrl = "http://localhost:9000/realms/spring-boot-code/protocol/openid-connect/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("code", request.getCode());
            params.add("redirect_uri", request.getRedirectUri());
            params.add("code_verifier", request.getCodeVerifier());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            ResponseEntity<Map> response = new RestTemplate().postForEntity(
                    tokenUrl,
                    new HttpEntity<>(params, headers),
                    Map.class
            );

            Map<String, Object> tokens = response.getBody();
            String accessToken = (String) tokens.get("access_token");

            // Decode không verify
            DecodedJWT decodedJWT = JWT.decode(accessToken);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("sub", decodedJWT.getSubject());
            userInfo.put("email", decodedJWT.getClaim("email").asString());
            userInfo.put("preferred_username", decodedJWT.getClaim("preferred_username").asString());
            userInfo.put("family_name", decodedJWT.getClaim("family_name").asString());
            userInfo.put("given_name", decodedJWT.getClaim("given_name").asString());
            userInfo.put("claims", decodedJWT.getClaims());

            if (userInfo == null || !userInfo.containsKey("email")) {
                return ResponseEntity.status(400).body(Map.of(
                        "error", "invalid_user_info",
                        "error_description", "User info response is invalid"
                ));
            }
            String email = (String) userInfo.get("email");
            String username = (String) userInfo.get("preferred_username");
            String fullName = (String) userInfo.get("name");
            String givenName = (String) userInfo.get("given_name");
            String familyName = (String) userInfo.get("family_name");
            if (usersRepository.existsByEmail(email)) {
                return ResponseEntity.ok(response.getBody()); // Nếu đã có, trả về token luôn
            }

            // Tạo user mới
            Users user = new Users();
            user.setEmail(email);
            user.setLastName(username);
            user.setLastName(fullName);
            user.setPassword(passwordEncoder.encode("10102003"));
            user.setActiveCode(null);
            user.setActive(true);
            user.setLastName(familyName);
            user.setFirstName(givenName);

            List<Roles> defaultRoles = rolesRepository.findByRoleName("ROLE_USER");
            user.setRolesList(defaultRoles);

            Users newUser = usersRepository.saveAndFlush(user);

            Cart cart = new Cart();
            cart.setUser(newUser);
            cart.setCartItem(null);
            cart.setDiscount(0);
            cart.setTotalItem(0);
            cart.setTotalDiscountedPrice(0);
            cart.setTotalPrice(0.0);
            this.cartRepository.saveAndFlush(cart);

            return ResponseEntity.ok().body(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "token_exchange_failed",
                    "error_description", e.getMessage()
            ));
        }
    }

//    @PostMapping("/token")
//    public ResponseEntity<?> exchangeCode(@RequestBody CodeExchangeRequest request) {
//        try {
//            String tokenUrl = "http://localhost:9000/realms/spring-boot-code/protocol/openid-connect/token";
//
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("grant_type", "authorization_code");
//            params.add("client_id", "authenticationClientId");
//            params.add("client_secret", "2Q0fzIMr6yEpEv3NkbL3XxWSTFz8I0fn");
//            params.add("code", request.getCode());
//            params.add("redirect_uri", request.getRedirectUri());
//            params.add("code_verifier", request.getCodeVerifier());
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            ResponseEntity<Map> response = new RestTemplate().postForEntity(
//                    tokenUrl,
//                    new HttpEntity<>(params, headers),
//                    Map.class
//            );
//
//            Map<String, Object> tokenResponse = response.getBody();
//            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
//                return ResponseEntity.status(400).body(Map.of(
//                        "error", "invalid_token_response",
//                        "error_description", "Token response is invalid"
//                ));
//            }
//
//            String accessToken = (String) tokenResponse.get("access_token");
//
//            String userInfoUrl = "http://localhost:9000/realms/spring-boot-code/protocol/openid-connect/userinfo";
//            HttpHeaders userHeaders = new HttpHeaders();
//            userHeaders.set("Authorization", "Bearer " + accessToken);
//
//            ResponseEntity<Map> userInfoResponse = new RestTemplate().exchange(
//                    userInfoUrl,
//                    HttpMethod.GET,
//                    new HttpEntity<>(userHeaders),
//                    Map.class
//            );
//
//            Map<String, Object> userInfo = userInfoResponse.getBody();
//            if (userInfo == null || !userInfo.containsKey("email")) {
//                return ResponseEntity.status(400).body(Map.of(
//                        "error", "invalid_user_info",
//                        "error_description", "User info response is invalid"
//                ));
//            }
//
//            String email = (String) userInfo.get("email");
//            String username = (String) userInfo.get("preferred_username");
//            String fullName = (String) userInfo.get("name");
//
//            // Kiểm tra nếu user đã tồn tại
//            if (usersRepository.existsByEmail(email)) {
//                return ResponseEntity.ok(tokenResponse); // Nếu đã có, trả về token luôn
//            }
//
//            // Tạo user mới
//            Users user = new Users();
//            user.setEmail(email);
//            user.setLastName(username);
//            user.setLastName(fullName);
//            user.setPassword(null);
//            user.setActiveCode(null);
//            user.setActive(true);
//
//            List<Roles> defaultRoles = rolesRepository.findByRoleName("ROLE_USER");
//            user.setRolesList(defaultRoles);
//
//            Users newUser = usersRepository.saveAndFlush(user);
//
//            return ResponseEntity.ok(tokenResponse);
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(Map.of(
//                    "error", "token_exchange_failed",
//                    "error_description", e.getMessage()
//            ));
//        }
//    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        try {
            if (!body.containsKey("refresh_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing refresh_token"));
            }

            String refreshToken = body.get("refresh_token");
            String logoutUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", "authenticationClientId");
            params.add("refresh_token", refreshToken);
            params.add("client_secret", clientSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            restTemplate.postForEntity(logoutUrl, request, String.class);

            return ResponseEntity.ok().body(Map.of("message", "Đăng xuất thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Logout failed", "details", e.getMessage()));
        }
    }

    private AccessToken verifyToken(String tokenString) throws VerificationException {
        return TokenVerifier.create(tokenString, AccessToken.class)
                .getToken();
    }
}