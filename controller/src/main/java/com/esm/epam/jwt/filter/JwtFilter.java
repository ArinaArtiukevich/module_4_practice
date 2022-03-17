package com.esm.epam.jwt.filter;

import com.esm.epam.entity.ErrorResponse;
import com.esm.epam.exception.JwtAuthenticationException;
import com.esm.epam.jwt.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.esm.epam.util.ParameterAttribute.CODE_AUTHENTICATION_EXCEPTION;
import static com.esm.epam.util.ParameterAttribute.NULL_STRING;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private JwtProvider jwtProvider;
    private UserDetailsService userDetailsService;
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException {
        try {
            Optional<String> token = jwtProvider.resolveToken((HttpServletRequest) req);
            if (token.isPresent() && jwtProvider.validateToken(token.get())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtProvider.getLoginFromToken(token.get()));
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, NULL_STRING, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(CODE_AUTHENTICATION_EXCEPTION);
            errorResponse.setMessage(e.getMessage());
            ((HttpServletResponse) res).setStatus(HttpStatus.FORBIDDEN.value());
            res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}