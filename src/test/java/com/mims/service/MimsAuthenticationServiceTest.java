package com.mims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mims.auth.LegacyLoginAccount;
import com.mims.auth.MimsUserPrincipal;
import com.mims.repository.LegacyLoginRepository;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MimsAuthenticationServiceTest {

    @Mock
    private LegacyLoginRepository repository;

    private MimsAuthenticationService service;

    @BeforeEach
    void setUp() {
        service = new MimsAuthenticationService(repository, new BCryptPasswordEncoder());
    }

    @Test
    void authenticatesLegacyPlaintextAndUpgradesItToBcrypt() {
        LegacyLoginAccount account = account("advisor-user", "cleartext", null, "Advisor");
        when(repository.findActiveByUsername("advisor-user")).thenReturn(Optional.of(account));
        when(repository.replacePassword(org.mockito.ArgumentMatchers.eq(12L),
                org.mockito.ArgumentMatchers.eq("cleartext"),
                org.mockito.ArgumentMatchers.anyString())).thenReturn(1);

        Optional<MimsUserPrincipal> principal = service.authenticate("advisor-user", "cleartext");

        assertTrue(principal.isPresent());
        assertEquals(12L, principal.get().getId());
        assertEquals("Advisor", principal.get().getUserType());
        verify(repository).replacePassword(org.mockito.ArgumentMatchers.eq(12L),
                org.mockito.ArgumentMatchers.eq("cleartext"),
                org.mockito.ArgumentMatchers.argThat(value ->
                        new BCryptPasswordEncoder().matches("cleartext", value)));
    }

    @Test
    void authenticatesLegacyInventorCiphertextAndUpgradesItToBcrypt() {
        String plaintext = "pwd";
        String keys = "3233O17O2753";
        LegacyLoginAccount account = account("inventor-user", encryptLegacy(plaintext, 17, 3233),
                keys, "Inventor");
        when(repository.findActiveByUsername("inventor-user")).thenReturn(Optional.of(account));
        when(repository.replacePassword(org.mockito.ArgumentMatchers.eq(12L),
                org.mockito.ArgumentMatchers.eq(account.password()),
                org.mockito.ArgumentMatchers.anyString())).thenReturn(1);

        Optional<MimsUserPrincipal> principal = service.authenticate("inventor-user", plaintext);

        assertTrue(principal.isPresent());
        verify(repository).replacePassword(org.mockito.ArgumentMatchers.eq(12L),
                org.mockito.ArgumentMatchers.eq(account.password()),
                org.mockito.ArgumentMatchers.argThat(value ->
                        new BCryptPasswordEncoder().matches(plaintext, value)));
    }

    @Test
    void acceptsBcryptPasswordWithoutRewritingIt() {
        String encoded = new BCryptPasswordEncoder().encode("secure-password");
        LegacyLoginAccount account = account("admin-user", encoded, null, "Admin");
        when(repository.findActiveByUsername("admin-user")).thenReturn(Optional.of(account));

        assertTrue(service.authenticate("admin-user", "secure-password").isPresent());
        verify(repository, never()).replacePassword(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
    }

    private LegacyLoginAccount account(String username, String password, String keys, String userType) {
        return new LegacyLoginAccount(12L, username, password, keys, userType, "Active");
    }

    private String encryptLegacy(String plaintext, int publicExponent, int modulus) {
        StringBuilder encrypted = new StringBuilder();
        for (byte value : plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8)) {
            if (!encrypted.isEmpty()) {
                encrypted.append('O');
            }
            encrypted.append(BigInteger.valueOf(value & 0xff)
                    .modPow(BigInteger.valueOf(publicExponent), BigInteger.valueOf(modulus)));
        }
        return encrypted.toString().replace('1', 'R');
    }
}
