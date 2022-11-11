/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service.impl;

import com.unab.FacturaEnLinea.model.Factura;
import com.unab.FacturaEnLinea.repository.FacturaRepository;
import com.unab.FacturaEnLinea.service.IFacturaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marlon
 */
@Service
public class FacturaService implements IFacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findAll() {
        return (List<Factura>) facturaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Factura> findAll(Pageable pageable) {
        return facturaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findById(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public void delete(Long id) {
        facturaRepository.deleteById(id);
    }
}
