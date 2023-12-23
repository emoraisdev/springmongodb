package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

    public void deleteById(String id);
}
