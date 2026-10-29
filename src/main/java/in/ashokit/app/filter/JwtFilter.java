package in.ashokit.app.filter;


import in.ashokit.app.exception.TokenExpiredException;

import in.ashokit.app.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt)) {
                    // Set authentication context
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                }
            }
            chain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            PrintWriter out = response.getWriter();
            out.println("JWT Token expired.....");
            out.close();
        }
    }
}