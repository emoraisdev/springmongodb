package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

    public void deleteById(String id);

    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    public List<Artigo> findByStatusEquals(Integer status);

    @Query("{$and: [{'data': {$gte: ?0}}, {'data': {$lte: ?1}}]}")
    public List<Artigo> getArtigoPorDataHora(LocalDateTime dataDe, LocalDateTime dataAte);

    Page<Artigo> findAll(Pageable pageable);

    public List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    // sort: 1 - ascendente
    // sort: -1 - descendente
    @Query(value= "{ 'status': { $eq: ?0 } }", sort = "{ 'titulo' : 1 }")
    public List<Artigo> getArtigoPorStatusOrdenado(Integer status);
}
