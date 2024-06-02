package com.labdessoft.roteiro01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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
    @Size(min = 1)
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