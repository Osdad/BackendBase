/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.repository;

import com.unab.FacturaEnLinea.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Marlon
 */
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
}
