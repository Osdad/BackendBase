/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.controller;
   
import com.unab.FacturaEnLinea.model.Cliente;
import com.unab.FacturaEnLinea.model.User;
import com.unab.FacturaEnLinea.repository.UserRepository;
import com.unab.FacturaEnLinea.request.JwtRequest;
import com.unab.FacturaEnLinea.request.JwtResponse;
import com.unab.FacturaEnLinea.security.JwtTokenUtil;
import com.unab.FacturaEnLinea.service.impl.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Marlon
 */
@RestController 
@CrossOrigin("*")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
     
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/user")
    public List<User> list() {
        return userService.findAll();
    }
    @GetMapping("/user/page/{page}")
    public Page<User> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return userService.findAll(pageable);
    }
    
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> get(@PathVariable String id) {
        User user = null;
        Map<String, Object> response = new HashMap<>();

        try {
            user = userService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (user == null) {
            response.put("mensaje", "Usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/ingreso")//No valida con la base de datos
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        User user = new User();
        user.setUsername(username);
        String token = getJWTToken(user);
        user.setToken(token);
        return user;
    }
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        System.out.println("Intento de Login");
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        User user = userRepository.findByUsername(userDetails.getUsername());
        //final String token = jwtTokenUtil.generateToken(userDetails);
        final  String token = getJWTToken(user);

        return ResponseEntity.ok(new JwtResponse(token, String.valueOf(user.getUsername()), user.getNombreCompleto()));
    }
    
     @PutMapping("user/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody User user, BindingResult result, @PathVariable String id) {
        User userActual = userService.findById(id);

        User userActualizado = null;

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (userActual == null) {
            response.put("mensaje", "Error: no se pudo editar, el cliente ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {

            userActual.setUsername(user.getUsername());
            userActual.setNombreCompleto(user.getNombreCompleto());
            userActual.setProfesion(user.getProfesion());
            userActual.setUrlFoto(user.getUrlFoto());
            userActualizado = userService.save(userActual);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido actualizado con éxito!");
        response.put("cliente", userActualizado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        System.out.println(user);
        return ResponseEntity.ok(userService.save(user));
    }
    
    @DeleteMapping("user/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            //Cliente cliente = clienteService.findById(id);
            userService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente eliminado con éxito!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
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
