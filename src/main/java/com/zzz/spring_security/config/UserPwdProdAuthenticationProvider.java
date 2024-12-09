package com.zzz.spring_security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class UserPwdProdAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // fetch username & password that entered by user
        String username = authentication.getName();
        String rawPwd = authentication.getCredentials().toString();


        // fetch user details from database using username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // validate entered password with hashed password
        String hashPwd = userDetails.getPassword();
        if (passwordEncoder.matches(rawPwd, hashPwd))
            // we don't return only "authentication" again, to add roles.
            return new UsernamePasswordAuthenticationToken(username, rawPwd, userDetails.getAuthorities());
        else
            throw new BadCredentialsException("Invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
