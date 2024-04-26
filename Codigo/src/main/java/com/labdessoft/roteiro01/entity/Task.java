package com.labdessoft.roteiro01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;// ID da tarefa;
    private String description;// Descrição da tarefa;
    private boolean completed;// A tarefa está completada ou não;
    private Priority priority;// Prioridade da tarefa;
    private LocalDate creationDate = LocalDate.now();// Data de criação da tarefa;
    private String status;// Campo para armazenar o status da tarefa;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public enum Priority{
        ALTA,
        MEDIA,
        BAIXA
    }
}