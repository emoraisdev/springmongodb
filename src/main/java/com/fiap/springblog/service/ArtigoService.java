package com.fiap.springblog.service;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.AutorTotalArtigo;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> getAll();

    public Artigo getById(String id);

//    public Artigo create(Artigo artigo);

    public ResponseEntity<?> create(Artigo artigo);

    public List<Artigo> findByDataGreaterThan(LocalDateTime data);

    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

    public ResponseEntity<?> update(Artigo artigo);

    public void updateArtigo(String id, String novaUrl);

    public void delete(String id);

    public void deleteArtigo(String id);

    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    public List<Artigo> getArtigoPorDataHora(LocalDateTime dataDe, LocalDateTime dataAte);

    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo);

    Page<Artigo> findAll(Pageable pageable);

    public List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    public List<Artigo> getArtigoPorStatusOrdenado(Integer status);

    public List<Artigo> findByTexto(String palavraChave);

    public List<ArtigoStatusCount> contarArtigosPorStatus();

    public List<AutorTotalArtigo> contarArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim);
}
