package com.mims.auth;

import com.mims.service.MimsAuthenticationService;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MimsAuthenticationProvider implements AuthenticationProvider {

    private final MimsAuthenticationService authenticationService;

    public MimsAuthenticationProvider(MimsAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials() == null
                ? null
                : authentication.getCredentials().toString();
        Optional<MimsUserPrincipal> principal = authenticationService.authenticate(username, password);
        if (principal.isEmpty()) {
            throw new BadCredentialsException("Invalid user ID or password");
        }
        return UsernamePasswordAuthenticationToken.authenticated(
                principal.get(), null, principal.get().getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
