package com.labdessoft.roteiro01.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public abstract class Task {
    private long id;// ID da tarefa;
    private String description;// Descrição da tarefa;
    private boolean completed;// A tarefa está completada ou não;
    private Priority priority;// Prioridade da tarefa;
    private LocalDate creationDate = LocalDate.now();// Data de criação da tarefa;
    private String status;// Campo para armazenar o status da tarefa;

    public enum Priority{
        ALTA,
        MEDIA,
        BAIXA
    }
}