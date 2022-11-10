/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service;

import com.unab.FacturaEnLinea.model.Cliente;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Marlon
 */
public interface IClienteService {
    
    public List<Cliente> findAll();
    
    public Page<Cliente> findAll(Pageable pageable);
    
    public Cliente findById(Long id);
    
    public Cliente save(Cliente cliente);
    
    public void delete(Long id);
}
