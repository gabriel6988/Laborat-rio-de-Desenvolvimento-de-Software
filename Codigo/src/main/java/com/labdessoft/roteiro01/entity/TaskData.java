package com.labdessoft.roteiro01.entity;

import java.time.LocalDate;

public class TaskData extends Task{
    private LocalDate dueDate;// Data prevista de conclusão;

    // Construtor;
    public TaskData() {
        super();// Construtor da Superclasse;
    }

    public TaskData(long id, String description, boolean completed, Priority priority, LocalDate dueDate) {
        super();
        setId(id);
        setDescription(description);
        setCompleted(completed);
        setPriority(priority);
        setDueDate(dueDate);
    }

    // Getter e Setter;
    public LocalDate getDueDate() {return dueDate;}
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}