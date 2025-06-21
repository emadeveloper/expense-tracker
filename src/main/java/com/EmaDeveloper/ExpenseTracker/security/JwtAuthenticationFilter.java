package com.EmaDeveloper.ExpenseTracker.security;

import com.EmaDeveloper.ExpenseTracker.Jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // ¡AÑADE ESTA IMPORTACIÓN!
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userIdentifier;

        // 1. Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // If no token or not Bearer, continue the filter chain
            return; // Exit the method
        }

        // 2. Extract the JWT token
        jwt = authHeader.substring(7); // "Bearer " has 7 characters

        // 3. Extract the user identifier (username or email) from the token
        userIdentifier = jwtService.extractUsername(jwt);

        // 4. Check if the user is authenticated and the identifier is not null
        if (userIdentifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5. If the user is not authenticated, but the JWT is valid, load the user details
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userIdentifier);

            // 6. Validate the JWT token
            // Asumo que tu validateToken espera UserDetails. Si espera String, cambia a userDetails.getUsername()
            if (jwtService.isTokenValid(jwt, userDetails)) { // <-- ¡IMPORTANTE! Revisa la firma de tu validateToken

                // 7. If the token is valid, create an authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials are null after token validation
                        userDetails.getAuthorities() // User's roles/authorities
                );

                // 8. Set authentication details (e.g., client IP) for Spring Security
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. Set the authentication object in the SecurityContextHolder
                // This "authenticates" the user for the current request
                SecurityContextHolder.getContext().setAuthentication(authToken); // <-- ¡AÑADE ESTA LÍNEA!
            }
        }

        // 10. Continue with the filter chain (to other filters or the DispatcherServlet)
        filterChain.doFilter(request, response); // <-- ¡AÑADE ESTA LÍNEA!
    }
}