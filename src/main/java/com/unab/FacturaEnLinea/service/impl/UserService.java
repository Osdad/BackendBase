/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service.impl;

import com.unab.FacturaEnLinea.model.User;
import com.unab.FacturaEnLinea.repository.UserRepository;
import com.unab.FacturaEnLinea.service.IUserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marlon
 */
@Service
public class UserService implements UserDetailsService, IUserService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder bcryptEncoder;
    
    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
           return (Page<User>) userRepo.findAll(pageable);
    }

    @Override
    public User findById(String id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        User usuario=new User();
        usuario.setUsername(user.getUsername().toLowerCase());
        usuario.setPassword(bcryptEncoder.encode(user.getPassword()));
        usuario.setNombreCompleto(user.getNombreCompleto().toUpperCase());
        usuario.setProfesion(user.getProfesion().toUpperCase());
        usuario.setUrlFoto(user.getUrlFoto());
        usuario.setDescripcionServicio(user.getDescripcionServicio());
        
        
        //usuario.setFechaCreacion(user.getFechaCreacion());
        //return userRepo.save(user);
        //user.setUsername(user.getUsername().toLowerCase());
        //user.setPassword(bcryptEncoder.encode(user.getPassword()));
        //user.setNombreCompleto(user.getNombreCompleto().toUpperCase());
        //user.setFechaCreacion(new Date());
        return userRepo.save(usuario);
    }

    @Override
    public void delete(String id) {
        userRepo.deleteById(id);
    }
    
}
