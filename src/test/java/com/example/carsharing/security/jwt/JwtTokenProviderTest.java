package com.example.carsharing.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.carsharing.model.User;
import com.example.carsharing.security.CustomUserDetailsService;
import com.example.carsharing.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteWt5dGFrYXRrb3ZAZ21haWw"
            + "uY29tIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2ODkwOTE3NTcsImV4cCI6MTY4OTA5NTM1N30."
            + "lHzZY9q5qj79PvMm7ukKOIGUUccYsazLN737m3xRxjc";
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Claims claims = Jwts.claims().setSubject(EMAIL);
        List<String> roles = new ArrayList<>();
        roles.add(User.Role.MANAGER.name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 36000L);
        Mockito.when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(now).setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes())).compact());
        String token = jwtTokenProvider.createToken(EMAIL, roles);
        assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(User.Role.CUSTOMER);
        Mockito.doReturn(EMAIL).when(jwtTokenProvider).getUsername(TOKEN);
        Mockito.when(userService.getByEmail(EMAIL)).thenReturn(Optional.of(user));
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        assertNotNull(authentication);
    }

    @Test
    public void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String token = jwtTokenProvider.resolveToken(request);
        assertEquals(TOKEN, token);
    }

    @Test
    public void resolveToken_invalidToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }

    @Test
    void validateToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        String token = "eyJhbGciOiJIUzI1NiJ9"
                + ".eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY4OTAxMjIz"
                + "MiwiZXhwIjoxNjg5MDE1ODMyfQ"
                + ".hkdG6A8tbLn2qYdi9h0HJguQYXtPYL4QFHQOgjf7VKE";
        Mockito.doReturn(true).when(jwtTokenProvider).validateToken(token);
        boolean valid = jwtTokenProvider.validateToken(token);
        assertTrue(valid);
    }

    @Test
    public void testValidateToken_ExpiredToken_notOk() {
        try {
            jwtTokenProvider.validateToken(TOKEN);
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected to receive RuntimeException");
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String token = "invalid token";
        try {
            jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected to receive RuntimeException");
    }
}
