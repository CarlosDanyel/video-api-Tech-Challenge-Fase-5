package com.fiapx.api.application;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TokenServiceTest {
    private final TokenService tokens = new TokenService("test-secret-with-at-least-32-characters", new ObjectMapper());
    @Test void issuedTokenAuthenticatesItsOwner() {
        UUID owner = UUID.randomUUID();
        assertEquals(owner, tokens.verify(tokens.issue(owner)));
    }
    @Test void tamperedTokenIsRejected() {
        String token = tokens.issue(UUID.randomUUID());
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertThrows(IllegalArgumentException.class, () -> tokens.verify(tampered));
    }
    @Test void weakSecretIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TokenService("short", new ObjectMapper()));
    }
}
