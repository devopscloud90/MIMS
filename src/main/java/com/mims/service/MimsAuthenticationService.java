package com.mims.service;

import com.mims.auth.LegacyLoginAccount;
import com.mims.auth.MimsUserPrincipal;
import com.mims.repository.LegacyLoginRepository;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MimsAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(MimsAuthenticationService.class);

    private final LegacyLoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public MimsAuthenticationService(LegacyLoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<MimsUserPrincipal> authenticate(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return Optional.empty();
        }

        return loginRepository.findActiveByUsername(username)
                .filter(account -> matches(account, rawPassword))
                .map(account -> {
                    if (!isBcrypt(account.password())) {
                        loginRepository.replacePassword(account.id(), account.password(),
                                passwordEncoder.encode(rawPassword));
                    }
                    return new MimsUserPrincipal(account.id(), account.username(), account.userType());
                });
    }

    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<LegacyLoginAccount> existing = loginRepository.findById(userId);
        if (existing.isEmpty() || !matches(existing.get(), currentPassword)) {
            return false;
        }
        return loginRepository.updatePassword(userId, passwordEncoder.encode(newPassword)) == 1;
    }

    public boolean resetPassword(Long userId, String newPassword) {
        return loginRepository.findById(userId)
                .map(account -> loginRepository.updatePassword(
                        userId, passwordEncoder.encode(newPassword)) == 1)
                .orElse(false);
    }

    private boolean matches(LegacyLoginAccount account, String rawPassword) {
        if (isBcrypt(account.password())) {
            return passwordEncoder.matches(rawPassword, account.password());
        }

        String storedPassword = account.password();
        if ("Inventor".equalsIgnoreCase(account.userType())) {
            storedPassword = decryptLegacyPassword(account.password(), account.keys());
        }
        if (storedPassword == null) {
            return false;
        }
        return MessageDigest.isEqual(
                storedPassword.getBytes(StandardCharsets.UTF_8),
                rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isBcrypt(String storedPassword) {
        return storedPassword != null && storedPassword.matches("^\\$2[aby]\\$\\d{2}\\$.*");
    }

    private String decryptLegacyPassword(String encrypted, String serializedKeys) {
        if (encrypted == null || serializedKeys == null) {
            return null;
        }
        try {
            StringTokenizer tokens = new StringTokenizer(serializedKeys, "O");
            if (tokens.countTokens() < 3) {
                return null;
            }
            BigInteger modulus = new BigInteger(tokens.nextToken());
            tokens.nextToken();
            BigInteger privateExponent = new BigInteger(tokens.nextToken());
            StringBuilder plaintext = new StringBuilder();
            StringTokenizer encryptedTokens = new StringTokenizer(encrypted.replace('R', '1'), "O");
            while (encryptedTokens.hasMoreTokens()) {
                BigInteger value = new BigInteger(encryptedTokens.nextToken());
                plaintext.append((char) value.modPow(privateExponent, modulus).intValue());
            }
            return plaintext.toString();
        } catch (IllegalArgumentException | ArithmeticException exception) {
            logger.warn("Unable to decode legacy credentials for account due to invalid key data");
            return null;
        }
    }
}
