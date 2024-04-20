package com.labdessoft.roteiro01.entity;

public class TaskPrazo extends Task {
    private int deadlineInDays;// Prazo previsto de conclusão informado em dias;

    // Construtor;
    public TaskPrazo() {
        super();// Construtor da Superclasse;
    }

    public TaskPrazo(long id, String description, boolean completed, Priority priority, int deadlineInDays) {
        super();
        setId(id);
        setDescription(description);
        setCompleted(completed);
        setPriority(priority);
        setDeadlineInDays(deadlineInDays);
    }

    // Getter e Setter;
    public int getDeadlineInDays() {
        return deadlineInDays;
    }
    public void setDeadlineInDays(int deadlineInDays) {
        this.deadlineInDays = deadlineInDays;
    }
}