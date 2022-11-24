/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service;

import com.unab.FacturaEnLinea.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Marlon
 */
public interface IUserService {
    
    public List<User> findAll();
    
    public Page<User> findAll(Pageable pageable);
    
    public User findById(String id);
    
    public User save(User user);
    
    public void delete(String id);
}
