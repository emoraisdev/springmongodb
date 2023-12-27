package com.fiap.springblog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Artigo {

    @Id
    private String id;

    @NotBlank(message = "O Título não pode ser vazio.")
    private String titulo;

    @NotNull(message = "A Data não pode ser nula.")
    private LocalDateTime data;

    @TextIndexed
    @NotBlank(message = "O Texto não pode ser vazio.")
    private String texto;

    private String url;

    @NotNull(message = "O Status não pode ser nulo.")
    private Integer status;

    @DBRef
    private Autor autor;

    @Version
    private Long version;
}
