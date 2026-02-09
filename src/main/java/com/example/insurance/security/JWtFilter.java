package com.example.insurance.security;

import com.example.insurance.service.UserLoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Date;

@Component
public class JWtFilter extends OncePerRequestFilter {
    @Autowired
    JWtUtil jWtUtil;

    @Autowired
    UserLoginService userLoginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = auth.substring(7);
        try {
            String userName = jWtUtil.extractUserName(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userLoginService.loadUserByUsername(userName);
                if (jWtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch(Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
            }
        filterChain.doFilter(request, response);
    }
}
