/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.controller;
 
import com.unab.FacturaEnLinea.model.User;
import com.unab.FacturaEnLinea.repository.UserRepository;
import com.unab.FacturaEnLinea.request.JwtRequest;
import com.unab.FacturaEnLinea.request.JwtResponse;
import com.unab.FacturaEnLinea.security.JwtTokenUtil;
import com.unab.FacturaEnLinea.service.impl.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Marlon
 */
@RestController 
public class UserRestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
     
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/mao")
    public String Saludo(){
        return "Hola";
    }
    @PostMapping("/user")//No valida con la base de datos
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        User user = new User();
        user.setUsername(username);
        String token = getJWTToken(user);
        user.setToken(token);
        return user;
    }
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        User user = userRepository.findByUsername(userDetails.getUsername());
        //final String token = jwtTokenUtil.generateToken(userDetails);
        final  String token = getJWTToken(user);

        return ResponseEntity.ok(new JwtResponse(token, String.valueOf(user.getUsername()), user.getNombreCompleto()));
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        System.out.println(user);
        return ResponseEntity.ok(userService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    //el metodo que se encarga de crear el TOKEN
    private String getJWTToken(User user) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId(user.getUsername())
				.setSubject(user.getNombreCompleto())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

}
