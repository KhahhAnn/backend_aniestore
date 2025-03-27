package com.khahhann.backend.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakAuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String exchangeGoogleToken(String googleAccessToken) {
        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        requestBody.add("subject_token", googleAccessToken);
        requestBody.add("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");
        requestBody.add("subject_issuer", "google");
        requestBody.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            return response.getBody().get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException("Token exchange failed: " + e.getMessage());
        }
    }
}
