/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.request;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Marlon
 */
//@Data
public class UserRequest  implements Serializable{
    @Autowired
    private PasswordEncoder bcryptEncoder;
    private String username;
    private String password;
    private String nombreCompleto;
    private String profesion;    
    private String descripcionServicio;
    private String urlFoto;

    public UserRequest(String username, String password, String nombreCompleto, String profesion, String descripcionServicio, String urlFoto) {
        this.username = username;
        this.password = bcryptEncoder.encode(password);
        this.nombreCompleto = nombreCompleto;
        this.profesion = profesion;
        this.descripcionServicio = descripcionServicio;
        this.urlFoto = urlFoto;
    }
    
    

    public String getPassword() {
        this.password = bcryptEncoder.encode(password);
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
    
    
    
    
    
}
