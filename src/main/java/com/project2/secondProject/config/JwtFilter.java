package com.project2.secondProject.config;

import com.project2.secondProject.service.JWTService;
import com.project2.secondProject.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtFilter intercepts every incoming HTTP request once,
 * checks for a valid JWT token, validates it, and sets
 * authentication in the Spring Security context.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Get Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2️⃣ Extract token from Bearer header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUserName(token);
            } catch (Exception e) {
                System.out.println("❌ JWT extraction error: " + e.getMessage());
            }
        }

        // 3️⃣ Validate token and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Get user details from custom service
            MyUserDetailsService userDetailsService = context.getBean(MyUserDetailsService.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ✅ Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4️⃣ Always continue filter chain (important)
        filterChain.doFilter(request, response);
    }
}
