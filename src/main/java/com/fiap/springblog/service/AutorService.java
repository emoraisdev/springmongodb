package com.fiap.springblog.service;

import com.fiap.springblog.model.Autor;

import java.util.List;

public interface AutorService {
    public Autor create(Autor autor);

    public Autor getById(String id);

    public List<Autor> getAll();
}
