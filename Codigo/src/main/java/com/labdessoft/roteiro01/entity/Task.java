package com.labdessoft.roteiro01.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class Task {
    private long id;// ID da tarefa;
    private String description;// Descrição da tarefa;
    private boolean completed;// Status da tarefa;
    private TaskType taskType;// Tipo da tarefa;
    private Priority priority;// Prioridade da tarefa;

    private LocalDate creationDate = LocalDate.now();// Data de criação da tarefa;
    private LocalDate dueDate;// Nova propriedade para tarefas do tipo "Data";
    private int deadlineInDays;// Nova propriedade para tarefas do tipo "Prazo";
    private String status;// Campo para armazenar o status da tarefa;

    public enum TaskType {
        DATA,
        PRAZO,
        LIVRE
    }
    public enum Priority{
        ALTA,
        MEDIA,
        BAIXA
    }
}