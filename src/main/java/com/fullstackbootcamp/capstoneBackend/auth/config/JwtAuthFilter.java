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

    /*
    This method doFilterInternal is the heart of the filter. It is called for each HTTP request to process JWT authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Retrieve Authorization header
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        LOGGER.info("Authorization Header: {}", authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            LOGGER.warn("No valid Authorization header found. Proceeding without authentication.");
            filterChain.doFilter(request, response);
            return;
        }


        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.extractUserUsernameFromToken(authorizationHeader);

        // Load custom user details
        UserDetails user = customUserDetailsService.loadUserByUsername(username);

        // This line creates an Authentication object (UsernamePasswordAuthenticationToken) with the user details and authorities (roles and permissions) obtained from the database.
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // It sets the authentication details, including the remote address and session ID, using WebAuthenticationDetailsSource.
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Finally, it sets the authenticated Authentication object in the Spring Security SecurityContextHolder, indicating that the user is authenticated and authorized to access protected resources.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // This line allows the request to continue processing by passing it along the filter chain.
        filterChain.doFilter(request,response);
    }
}