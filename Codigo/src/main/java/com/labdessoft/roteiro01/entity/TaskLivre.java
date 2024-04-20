package com.labdessoft.roteiro01.entity;

public class TaskLivre extends Task{
    // Construtor;
    public TaskLivre() {
        super();// Construtor da Superclasse;
    }
    public TaskLivre(long id, String description, boolean completed, Priority priority) {
        super();
        setId(id);
        setDescription(description);
        setCompleted(completed);
        setPriority(priority);
    }
}