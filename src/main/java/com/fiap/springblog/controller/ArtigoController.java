package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.service.AutorService;
import com.fiap.springblog.service.impl.ArtigoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService service;

    @GetMapping
    public List<Artigo> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Artigo getById(@PathVariable String id){
        return service.getById(id);
    }

    @PostMapping
    public Artigo create(@RequestBody Artigo artigo){
        return service.create(artigo);
    }

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam("data") LocalDateTime data){
        return service.findByDataGreaterThan(data);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(@RequestParam("data") LocalDateTime data,
                                              @RequestParam("status") Integer status){
        return service.findByDataAndStatus(data, status);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Artigo artigo){
        service.update(artigo);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUrl(@PathVariable String id,
                                    @RequestBody String url){
        service.updateArtigo(id, url);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id){

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteArtigo(@PathVariable String id){

        service.deleteArtigo(id);

        return ResponseEntity.noContent().build();
    }
}
