package com.zzz.spring_security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/* Filter job:
    - validate that input email not have "test" word inside it.
    - We want to put this filter before authentication filter. */

public class emailValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // cast request and response objects to their HTTP-specific counterparts to can use http functionalities.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // catch the value of Authorization header
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);

        // validate the structure of header value
        if (authHeader == null || !authHeader.trim().startsWith("Basic ")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("Invalid Authorization header.");
            return;
        }

        // remove null spaces from edges if exist
        authHeader = authHeader.trim();

        // Base64 encoding operates on bytes. so must change token form into byte array.
        byte[] base64Token = authHeader.substring(6).getBytes(StandardCharsets.UTF_8);

        // Decode the token and convert it into string form
        String username;
        try {
            byte[] decodedToken = Base64.getDecoder().decode(base64Token);
            String token = new String(decodedToken, StandardCharsets.UTF_8);

            // extract username from the token (token => "username:password")
            int delim = token.indexOf(":");

            if (delim == -1) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Invalid Authorization header.");
                return;
            }

            // catch the username only with help of delimiter
            username = token.substring(0, delim);

            } catch (IllegalArgumentException exception) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Failed to decode basic authentication token.");
                return;
        }

        // Check the username contain "test" word or not
        if (username.toLowerCase().contains("test")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("the email contains 'test' word.");
            return;
        }

        // invoke the next filter
        chain.doFilter(request, response);
    }
}
