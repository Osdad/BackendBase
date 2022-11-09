/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.repository;

import com.unab.FacturaEnLinea.model.User; 
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Marlon
 */
public interface UserRepository extends JpaRepository<User, String> {
     User findByUsername(String username);
}
