package com.fiap.springblog.service;

import com.fiap.springblog.model.Artigo;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> getAll();

    public Artigo getById(String id);

    public Artigo create(Artigo artigo);

    public List<Artigo> findByDataGreaterThan(LocalDateTime data);

    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

    public void update(Artigo artigo);

    public void updateArtigo(String id, String novaUrl);

    public void delete(String id);

    public void deleteArtigo(String id);
}
