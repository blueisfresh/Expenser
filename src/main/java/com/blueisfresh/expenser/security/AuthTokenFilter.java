package com.blueisfresh.expenser.security;

import com.blueisfresh.expenser.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// Acts as the interceptor for JWTs on every request.
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = parseJwt(request);

        // Only proceed with JWT validation if a JWT token is actually present
        if (jwt != null) {
            try {
                if (jwtUtil.validateJwtToken(jwt)) {
                    String username = jwtUtil.getUsernameFromToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) { // This catch now only applies if a JWT was present and invalid
                // Log the error for invalid JWTs. Do NOT rethrow or send 401 directly here.
                // Spring Security's AuthenticationEntryPoint will handle the 401 for unauthorized access
                // or you can log.
                System.out.println("Invalid JWT processing error: " + e.getMessage());
                // Crucially, don't throw AuthenticationException from here for *this* catch.
                // Let Spring Security's normal flow handle unauthorized.
            }
        }
        // Always continue the filter chain, allowing subsequent filters (like Spring Security's
        // AuthorizationFilter or Spring MVC's validation) to do their job.
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
