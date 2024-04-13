package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Adiciona tasks no repositório;
    @PostMapping("/")
    @Operation(summary = "Cadastra tarefas")
    public ResponseEntity<String> addTask(@RequestBody Task task) {
        if (task.getTaskType() == Task.TaskType.DATA && LocalDate.now().isAfter(task.getDueDate())) {
            return ResponseEntity.badRequest().body("A data prevista deve ser igual ou superior à data atual.");
        }
        taskRepository.save(task);
        return ResponseEntity.ok("Tarefa adicionada com sucesso.");
    }

    // Adquire todas as tasks;
    @GetMapping("/")
    @Operation(summary = "Lista todas as tarefas da lista")
    public List<Task> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            task.setStatus(getTaskStatus(task));
        }
        return tasks;
    }

    // Método auxiliar para obter o status da tarefa com base em seu tipo;
    private String getTaskStatus(Task task) {
        switch (task.getTaskType()) {
            case DATA:
                LocalDate currentDate = LocalDate.now();// Data atual;
                if (task.isCompleted()) {
                    return "Concluída";
                } else if (currentDate.isAfter(task.getDueDate())) {
                    return ChronoUnit.DAYS.between(task.getDueDate(), currentDate) + " dias de atraso";
                } else {
                    return "Prevista";
                }
            case PRAZO:
                LocalDate currentDate2 = LocalDate.now();// Data atual;
                LocalDate predictedDate = task.getCreationDate().plusDays(task.getDeadlineInDays());
                if (task.isCompleted()) {
                    return "Concluída";
                } else if (currentDate2.isAfter(predictedDate)) {
                    return ChronoUnit.DAYS.between(predictedDate, currentDate2) + " dias de atraso";
                } else {
                    return "Prevista";
                }
            case LIVRE:
                return task.isCompleted() ? "Concluída" : "Prevista";
            default:
                return "Status não definido";
        }
    }

    // Marca a task como completada;
    @PutMapping("/{id}/completed")
    @Operation(summary = "Completar tarefas")
    public void completeTask(@PathVariable long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setCompleted(true);// Marca a tarefa como concluída;
            taskRepository.save(task);
        }
    }

    // Modifica a task;
    @PutMapping("/{id}")
    @Operation(summary = "Editar tarefas")
    public void updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            taskRepository.save(task);
        }
    }

    // Excluir tasks;
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma tarefa específica")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
            return ResponseEntity.ok("Tarefa deletada com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir tasks concluídas;
    @DeleteMapping("/completed")
    @Operation(summary = "Deletar tarefas concluídas")
    public void deleteCompletedTasks() {
        // Get all tasks
        List<Task> allTasks = taskRepository.findAll();

        // Iterate through all tasks and delete completed tasks
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                taskRepository.delete(task);
            }
        }
    }
}