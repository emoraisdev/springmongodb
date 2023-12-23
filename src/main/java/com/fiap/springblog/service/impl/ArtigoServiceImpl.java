package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository repo;

    @Autowired
    private AutorService autorService;

    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Artigo> getAll() {
        return repo.findAll();
    }

    @Override
    public Artigo getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado!"));
    }

    @Override
    public Artigo create(Artigo artigo) {


        if (artigo.getAutor()!= null && artigo.getAutor().getId() != null) {
            Autor autor = autorService.getById(artigo.getAutor().getId());

            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }

        return repo.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {

        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(
                Criteria.where("data").is(data)
                        .and("status").is(status));

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public void update(Artigo artigo) {
        repo.save(artigo);
    }

    @Override
    public void updateArtigo(String id, String novaUrl) {

        // Critério de busca
        Query query = new Query(Criteria.where("_id").is(id));

        // Definindo os campos que serão atualizados.
        Update update = new Update().set("url", novaUrl);

        //Efetivando a atualização.
        mongoTemplate.updateFirst(query, update, Artigo.class);

    }

    @Override
    public void delete(String id) {

        getById(id);
        repo.deleteById(id);
    }

    @Override
    public void deleteArtigo(String id) {
        // Critério de busca
        Query query = new Query(Criteria.where("_id").is(id));

        //Efetivando a atualização.
        mongoTemplate.remove(query, Artigo.class);
    }
}
