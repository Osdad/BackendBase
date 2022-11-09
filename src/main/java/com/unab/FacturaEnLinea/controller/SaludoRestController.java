/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Marlon
 */
@RestController
@RequestMapping()
public class SaludoRestController {
    @RequestMapping("hello")
    public String helloWorld(@RequestParam(value="name", defaultValue="Mundo") String name) {
            return "Hola "+name+"!!...Servicio Funcionando";
    }
}
 