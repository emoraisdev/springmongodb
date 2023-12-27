package com.fiap.springblog.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtigoComAutorRequest {

    @NotNull(message = "Os dados do Artigo são obrigatórios!")
    private Artigo artigo;

    @NotNull(message = "Os dados do Autor são obrigatórios!")
    private Autor autor;
}
