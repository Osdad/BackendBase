/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
 
/**
 *
 * @author Marlon
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Producto implements Serializable {
    
    @Id
    @GeneratedValue(generator = "UUID") 
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id; //AutoIncrementable de tipo String UUID
    
    @NotBlank @NotNull
    @Size(min = 4, max = 200, message = "el tamaño tiene que estar entre 4 y 12")
    private String nombreProducto; 
    
    @NotBlank @NotNull
    @Size(max=500)
    private String descripcionProducto;
    
    @NotNull @DecimalMin(value="0.0")
    private Double precioProducto;
    
    @NotBlank @NotNull()
    private String categoriaProducto;
    
    @NotBlank @NotNull()
    private String imagen;  
    
    private Integer ind_estado; //1:Activo y 0:Inactivo
    
}
