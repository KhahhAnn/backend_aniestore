package com.khahhann.backend.controller;

import com.khahhann.backend.request.CodeExchangeRequest;
import com.khahhann.backend.request.TokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class KeycloakAuth {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final Keycloak keycloak;

    public KeycloakAuth(Keycloak keycloak) {
        this.keycloak = keycloak;
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
            params.add("client_id", "authenticationClientId");
            params.add("client_secret", "2Q0fzIMr6yEpEv3NkbL3XxWSTFz8I0fn");
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

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "token_exchange_failed",
                    "error_description", e.getMessage()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            String logoutUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", "authenticationClientId");
            params.add("refresh_token", token);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(logoutUrl, params, String.class);

            return ResponseEntity.ok().body(Map.of("message", "Đăng xuất thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Logout failed"));
        }
    }

    private AccessToken verifyToken(String tokenString) throws VerificationException {
        return TokenVerifier.create(tokenString, AccessToken.class)
                .getToken();
    }
}