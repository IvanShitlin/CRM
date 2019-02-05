package com.foxminded.hipsterfox.service.amocrm;

import com.foxminded.hipsterfox.config.AmocrmProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AmocrmAuthHandler implements Serializable {

    private static final Long COOKIES_LIFETIME_MINUTES = 14L;

    private static HttpHeaders headersWithCookies;
    private static LocalDateTime lastHeadersSync;

    private final AmocrmProperties amocrmProperties;
    private final RestTemplate restTemplate;

    public AmocrmAuthHandler(AmocrmProperties amocrmProperties, @Qualifier("amocrmRestTemplate") RestTemplate restTemplate) {
        this.amocrmProperties = amocrmProperties;
        this.restTemplate = restTemplate;
    }

    public HttpHeaders getHeaders() {
        if (headersWithCookies == null || !isCookiesAlive()) {
            authenticate();
        }
        return headersWithCookies;

    }

    private void authenticate() {
        HttpEntity<String> request = new HttpEntity<>(
            createAuthHeaders(amocrmProperties.getConnection().getLogin(), amocrmProperties.getConnection().getPassword()));

        HttpEntity<?> response = restTemplate.exchange(
            amocrmProperties.getConnection().getFullAuthUrl(), HttpMethod.POST, request, String.class);

        HttpHeaders headers = response.getHeaders();

        HttpHeaders cookieHeaders = new HttpHeaders();
        cookieHeaders.add("Cookie", headers.getFirst(HttpHeaders.SET_COOKIE));
        headersWithCookies = cookieHeaders;
        lastHeadersSync = LocalDateTime.now();
    }

    private HttpHeaders createAuthHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            set("Authorization", "Basic " + encodedAuth);
        }};
    }

    private boolean isCookiesAlive() {
        return LocalDateTime.now().minusMinutes(COOKIES_LIFETIME_MINUTES).isBefore(lastHeadersSync);
    }

}
