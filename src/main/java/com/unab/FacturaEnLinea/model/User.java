/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author Marlon
 */
@Entity()
@Data
public class User {
    @Id()
    private String username;
    private String password;
    private String nombreCompleto;
    private String token;
   
    
}
