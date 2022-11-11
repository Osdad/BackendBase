/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.model; 
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Marlon
 */
@Entity()
@Data//lombok
public class User {
    @Id()
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "no puede estar vacio")
    @Size(min = 4, max = 12, message = "el tamaño tiene que estar entre 4 y 12")
    private String username;
    private String password;
    private String nombreCompleto;
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    private String token;
    
    @PrePersist
    public void prePersist() {
        System.out.println("Preguardado");
        this.fechaCreacion = new Date();
    }
   
    
}
