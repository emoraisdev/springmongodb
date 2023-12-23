package com.fiap.springblog.controller;

import com.fiap.springblog.model.Autor;
import com.fiap.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService service;

    @GetMapping
    public List<Autor> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Autor getById(@PathVariable String id){
        return service.getById(id);
    }

    @PostMapping
    public Autor create(@RequestBody Autor autor){
        return service.create(autor);
    }
}
