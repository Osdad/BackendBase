/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.security;
 
import com.unab.FacturaEnLinea.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Marlon
 */
@Component
public class JwtTokenUtil {
    private final static Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    
    //Clave para verificar el token
    @Value("${jwt.secret}")
    private String secret;

    //Tiempo base de expiración
    @Value("${jwt.expiration}")
    private int expiration;
    
     public String generateToken(Authentication authentication, User user){
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        logger.error(mainUser.getUsername());
        return Jwts.builder()
               .setId(mainUser.getUsername())
               .setSubject(user.getNombreCompleto())
               .setIssuedAt(new Date())
               .setExpiration(new Date(new Date().getTime() + expiration *1000))
               .signWith(SignatureAlgorithm.HS512, secret)
               .compact();
    }
     
    //Creamos una función que permita obtener el nombre de usuario con el token
    public String getUserNameFromToken(String token){
        //OBTENER ASUNTO
        //Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject()
        //OBTENER ID
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
        
    }

    //Creamos una función que permita validar nuestro token con la firma secreta
    //Controlamos cualquier error que pueda existir con el token

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("token mal formado");
        }catch (UnsupportedJwtException e){
            logger.error("token no soportado");
        }catch (ExpiredJwtException e){
            logger.error("token expirado");
        }catch (IllegalArgumentException e){
            logger.error("token vacío");
        }catch (SignatureException e){
            logger.error("fallo en la firma");
        }
        return false;
    }
}
