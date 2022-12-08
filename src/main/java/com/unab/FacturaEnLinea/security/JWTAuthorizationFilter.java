/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.security;

import com.unab.FacturaEnLinea.service.impl.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.WebUtils;

/**
 *
 * @author Marlon
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    @Autowired
    private  JwtTokenUtil jwtProvider;
    @Autowired
    private  UserService userServiceImpl;
    
    /*@Value("${jwt.accessTokenCookieName}")
    private String cookieName;*/


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)  throws ServletException, IOException {
         try {
            String token = getToken(req);
            if (token != null && jwtProvider.validateToken(token)) {
                String userName = jwtProvider.getUserNameFromToken(token);
                UserDetails userDetails = userServiceImpl.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(req, res);
    }
    
    private String getToken(HttpServletRequest request){
        /*Cookie cookie = WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue():null;*/
        final String requestTokenHeader = request.getHeader("Authorization");
        //Capturo el Token desde el encabezado
        System.out.println("Token Recibido: ");
        System.out.println(requestTokenHeader);
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                        username = jwtProvider.getUserNameFromToken(jwtToken);

                } catch (IllegalArgumentException e) {
                        System.out.println("Unable to get JWT Token");
                        return null;
                } catch (ExpiredJwtException e) {
                        System.out.println("JWT Token has expired");
                        return null;
                }
        } else {
                logger.warn("JWT Token does not begin with Bearer String");
                return null;
        }
        System.out.println("Token Retornado: ");
        System.out.println(jwtToken);
        return jwtToken;
    }
    
     
}
