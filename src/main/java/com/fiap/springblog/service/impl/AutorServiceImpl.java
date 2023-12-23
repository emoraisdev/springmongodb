package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Autor;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {


    @Autowired
    private AutorRepository repo;

    @Override
    public Autor create(Autor autor) {
        return repo.save(autor);
    }

    @Override
    public Autor getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));
    }

    @Override
    public List<Autor> getAll() {
        return repo.findAll();
    }
}
