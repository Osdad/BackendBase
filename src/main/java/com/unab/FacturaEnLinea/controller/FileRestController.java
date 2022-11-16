/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unab.FacturaEnLinea.controller;

import com.unab.FacturaEnLinea.model.FileModel;
import com.unab.FacturaEnLinea.request.FileMessage;
import com.unab.FacturaEnLinea.service.IFileService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


/**
 *
 * @author Marlon
 */
@RestController
@RequestMapping("/file")
@CrossOrigin("*")
public class FileRestController {
    @Autowired
    private IFileService fileService;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files")MultipartFile[] files){
         Map<String, Object> response = new HashMap<>();
        
        String message = "";
        try{
            List<String> fileNames = new ArrayList<>();
            int numero = (int)(Math. random()*10+1); //subir con el mismo nombre hay que arreglar esto
            Arrays.asList(files).stream().forEach(file->{
                fileService.save(file);
                fileNames.add(file.getOriginalFilename());
            });
            String archivo = fileNames.get(0);
            System.out.println(archivo);
            String url = MvcUriComponentsBuilder.fromMethodName(FileRestController.class, "getFile",
                  archivo).build().toString();
             response.put("mensaje", "Se subieron los archivos correctamente" + fileNames);
             response.put("archivo", url);
            //message = "Se subieron los archivos correctamente " + fileNames;
            //return ResponseEntity.status(HttpStatus.OK).body(new FileMessage(message));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch (Exception e){
            message = "Fallo al subir los archivos";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileMessage(message));
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<FileModel>> getFiles(){
        List<FileModel> fileInfos = fileService.loadAll().map(path -> {
          String filename = path.getFileName().toString();
          String url = MvcUriComponentsBuilder.fromMethodName(FileRestController.class, "getFile",
                  path.getFileName().toString()).build().toString();
          return new FileModel(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
    
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = fileService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }
    
    @GetMapping("/delete/{filename:.+}")
    public ResponseEntity<FileMessage> deleteFile(@PathVariable String filename) {
        String message = "";
        try {
            message = fileService.deleteFile(filename);
            return ResponseEntity.status(HttpStatus.OK).body(new FileMessage(message));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileMessage(message));
        }
    }
}
