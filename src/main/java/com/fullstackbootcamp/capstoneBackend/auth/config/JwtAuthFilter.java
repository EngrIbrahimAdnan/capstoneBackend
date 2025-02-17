package com.fullstackbootcamp.capstoneBackend.auth.config;

import com.fullstackbootcamp.capstoneBackend.auth.bo.CustomUserDetails;
import com.fullstackbootcamp.capstoneBackend.auth.service.CustomUserDetailsService;
import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class JwtAuthFilter extends OncePerRequestFilter {

    /*
    The JwtAuthFilter class defines two instance variables: jwtUtil and userDetailsService. These are required for JWT (JSON Web Token) authentication. The class has a constructor that receives these dependencies, allowing them to be injected when the filter is created.
     */
    private static final String BEARER = "Bearer ";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") ||
                path.startsWith("/setup/") ||
                path.startsWith("/ws/");
    }

    /*
    This method doFilterInternal is the heart of the filter. It is called for each HTTP request to process JWT authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip OPTIONS requests
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        LOGGER.info("Authorization Header: {}", authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            LOGGER.warn("No valid Authorization header found. Proceeding without authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtUtil.extractUserUsernameFromToken(authorizationHeader);
            UserDetails user = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            LOGGER.error("Authentication failed: ", e);
            // Don't set the security context if authentication fails
        }

        filterChain.doFilter(request, response);
    }
}