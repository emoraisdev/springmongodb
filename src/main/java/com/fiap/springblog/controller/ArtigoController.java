package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.service.AutorService;
import com.fiap.springblog.service.impl.ArtigoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService service;

    public ResponseEntity<String> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException exception){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de concorrência: O artigo foi atualizado por outro usuário!\n" +
                        exception.getMessage());
    }

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

    @GetMapping("/status-data-gt")
    public List<Artigo> findByStatusAndDataGreaterThan(@RequestParam("status") Integer status,
                                            @RequestParam("data") LocalDateTime data){
        return service.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/data-between")
    public List<Artigo> getArtigoPorDataHora(@RequestParam("datade") LocalDateTime datade,
                                                       @RequestParam("dataate") LocalDateTime dataate){
        return service.getArtigoPorDataHora(datade, dataate);
    }

    @GetMapping("/busca-complexa")
    public List<Artigo> encontrarArtigosComplexos(@RequestParam("status") Integer status,
                                             @RequestParam("data") LocalDateTime data,
                                                  @RequestParam("titulo") String titulo){
        return service.encontrarArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/findall")
    public ResponseEntity<Page<Artigo>> findAll(Pageable pageable){

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/status-orderby")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam("status") Integer status){
        return service.findByStatusOrderByTituloAsc(status);
    }

    @GetMapping("/status-ordenado")
    public List<Artigo> getArtigoPorStatusOrdenado(@RequestParam("status") Integer status){
        return service.getArtigoPorStatusOrdenado(status);
    }

    @GetMapping("/find-texto")
    public List<Artigo> findByTexto(@RequestParam("texto") String texto){
        return service.findByTexto(texto);
    }

    @GetMapping("/qtd-status")
    public List<ArtigoStatusCount> contarArtigosPorStatus(){
        return service.contarArtigosPorStatus();
    }

    @GetMapping("/autor-artigos")
    public List<AutorTotalArtigo> contarArtigosPorAutorNoPeriodo(@RequestParam("datainicio") LocalDate dataInicio,
                                                                 @RequestParam("datafim") LocalDate dataFim){
        return service.contarArtigosPorAutorNoPeriodo(dataInicio, dataFim);
    }

}
