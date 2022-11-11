/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service;

import com.unab.FacturaEnLinea.model.Factura;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Marlon
 */
public interface IFacturaService {
    
    public List<Factura> findAll();
    
    public Page<Factura> findAll(Pageable pageable);
    
    public Factura findById(Long id);
    
    public Factura save(Factura factura);
    
    public void delete(Long id);
}
