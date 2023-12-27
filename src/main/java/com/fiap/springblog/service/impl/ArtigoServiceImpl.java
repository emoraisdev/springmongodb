package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.service.AutorService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository repo;

    @Autowired
    private AutorRepository autorRepo;

    @Autowired
    private AutorService autorService;

    @Autowired
    private MongoTransactionManager transactionManager;

    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Artigo> getAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Artigo getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado!"));
    }

    @Override
    public ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor) {

        var transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(status -> {

            try {

                autorService.create(autor);

                artigo.setData(LocalDateTime.now());
                artigo.setAutor(autor);
                repo.save(artigo);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("Ocorreu um erro desconhecido!\n Erro:" + e.getMessage());
            }

            return null;
        });

        return null;
    }

    @Override
    public ResponseEntity<?> create(Artigo artigo) {

        if (artigo.getAutor()!= null && artigo.getAutor().getId() != null) {
            Autor autor = autorService.getById(artigo.getAutor().getId());

            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }

        try {
            repo.save(artigo);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (DuplicateKeyException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Artigo já existe na coleção");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro desconhecido: "+ e.getMessage());
        }

    }

/*    @Override
    @Transactional
    public Artigo create(Artigo artigo) {


        if (artigo.getAutor()!= null && artigo.getAutor().getId() != null) {
            Autor autor = autorService.getById(artigo.getAutor().getId());

            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }

        return repo.save(artigo);
    }*/

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
    @Transactional
    public ResponseEntity<?> update(Artigo artigo) {

        try {
            this.getById(artigo.getId());
            repo.save(artigo);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (OptimisticLockingFailureException ex) {

            try {
                var artigoAtualizado = this.getById(artigo.getId());

                artigoAtualizado.setTitulo(artigo.getTitulo());
                artigoAtualizado.setData(artigo.getData());
                artigoAtualizado.setTexto(artigo.getTexto());
                artigoAtualizado.setStatus(artigo.getStatus());
                artigoAtualizado.setUrl(artigo.getUrl());

                if (artigo.getAutor() != null) {
                    artigoAtualizado.setAutor(artigo.getAutor());
                }

                artigoAtualizado.setVersion(artigoAtualizado.getVersion());

                repo.save(artigoAtualizado);

                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateArtigo(String id, String novaUrl) {

        // Critério de busca
        Query query = new Query(Criteria.where("_id").is(id));

        // Definindo os campos que serão atualizados.
        Update update = new Update().set("url", novaUrl);

        //Efetivando a atualização.
        mongoTemplate.updateFirst(query, update, Artigo.class);

    }

    @Override
    @Transactional
    public void delete(String id) {

        getById(id);
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteArtigo(String id) {
        // Critério de busca
        Query query = new Query(Criteria.where("_id").is(id));

        //Efetivando a atualização.
        mongoTemplate.remove(query, Artigo.class);
    }

    @Override
    public void deleteArtigoEAutor(Artigo artigo) {

        new TransactionTemplate(transactionManager).execute(
                status -> {

                    try {
                        repo.delete(artigo);

                        autorRepo.delete(artigo.getAutor());
                        return null;
                    } catch (Exception e) {
                        status.setRollbackOnly();
                        throw new RuntimeException("Erro ao excluir artigo e autor: " + e.getMessage());
                    }
                }
        );
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return repo.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> getArtigoPorDataHora(LocalDateTime dataDe, LocalDateTime dataAte) {
        return repo.getArtigoPorDataHora(dataDe, dataAte);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {

        var criteria = new Criteria();
        criteria.and("data").lte(data);

        if (status != null) {
            criteria.and("status").is(status);
        }

        if (titulo != null && !titulo.isEmpty()) {
            criteria.and("titulo").regex(titulo, "i");
        }

        var query = new Query(criteria);

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> findAll(Pageable pageable) {

        var sort = Sort.by("titulo").ascending();
        var pageableOrdered = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), sort);
        return repo.findAll(pageableOrdered);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return repo.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> getArtigoPorStatusOrdenado(Integer status) {
        return repo.getArtigoPorStatusOrdenado(status);
    }

    @Override
    public List<Artigo> findByTexto(String palavraChave) {

        // Representa um critério
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(palavraChave);

        // sortByScore() - Vai ordenar pelo resultados mais relevantes, usando um determinado algoritimo.
        // Mecanismo semelhante ao Google.
        Query query = TextQuery.queryText(criteria).sortByScore();

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {

        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(Artigo.class,
                Aggregation.group("status").count().as("quantidade"),
                Aggregation.project("quantidade").and("status")
                        .previousOperation()
        );

        AggregationResults<ArtigoStatusCount> result =
                mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);

        return result.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> contarArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {

        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match(Criteria.where("data").gte(dataInicio)
                        .lt(dataFim.plusDays(1).atStartOfDay())),
                Aggregation.group("autor").count().as("totalArtigos"),
                Aggregation.project("totalArtigos").and("autor")
                        .previousOperation()
        );

        AggregationResults<AutorTotalArtigo> result =
                mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);

        return result.getMappedResults();
    }

}
