package com.labdessoft.roteiro01.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // ID da tarefa;

    @NotEmpty
    private String description; // Descrição da tarefa;
    private boolean completed; // A tarefa está completada ou não;

    @Enumerated(EnumType.STRING)
    private Priority priority; // Prioridade da tarefa;

    private LocalDate creationDate = LocalDate.now(); // Data de criação da tarefa;
    private String status; // Campo para armazenar o status da tarefa;

    public enum Priority {
        ALTA,
        MEDIA,
        BAIXA
    }
}