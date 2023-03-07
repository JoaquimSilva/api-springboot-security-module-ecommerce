package com.ecommerce.infrastruture.security;

import com.ecommerce.domain.model.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SecurityFilter  extends OncePerRequestFilter {
    private UserRepository userRepository;
    private TokenService tokenService;
    private static final String AUTHORIZATION =  "Authorization";
    private static final String BEARER =  "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = extractToken(request);


        if (null != token) {
            var user = userRepository.findByUserName(tokenService.getSubject(token));
            var authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        var authorization = request.getHeader(AUTHORIZATION);
        return null != authorization ? authorization.replace(BEARER, "") : null;
    }
}
