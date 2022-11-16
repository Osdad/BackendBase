/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.util.stream.Stream;
import java.nio.file.Path;

/**
 *
 * @author Marlon
 */
public interface IFileService {
    // Metodo para crear la carpeta donde vamos a guardar los archivos
    public void init();

    //Metodo para guardar los archivos
    public void save(MultipartFile file);

    //Metodo para cargar un archivo
    public Resource load(String filename);
    
    //Metodo para borrar todos los archivos cada vez que se inicie el servidor
    public void deleteAll();

    //Metodo para Cargar todos los archivos
    public Stream<Path> loadAll();

    //Metodo para Borrar un archivo
    public String deleteFile(String filename);
}
