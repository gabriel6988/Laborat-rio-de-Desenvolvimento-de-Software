package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.*;
import com.labdessoft.roteiro01.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    @Operation(summary = "Cadastra tarefas")
    public ResponseEntity<String> addTask(@RequestBody Task task) {
        String response = taskService.addTask(task);// Função para adicionar a tarefa;
        if (response.startsWith("Erro")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/")
    @Operation(summary = "Lista todas as tarefas da lista")
    public List<Task> listAllTasks() {
        return taskService.listAllTasks();// Função para listar todas as tarefas;
    }

    @PutMapping("/{id}/completed")
    @Operation(summary = "Completar tarefas")
    public void completeTask(@PathVariable long id) {
        taskService.completeTask(id);// Função para marcar a tarefa como completada;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar tarefas")
    public void updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        taskService.updateTask(id, updatedTask);// Função para editar a tarefa;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma tarefa específica")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        String response = taskService.deleteTask(id);// Função para deletar a tarefa;
        if (response.startsWith("Erro")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/completed")
    @Operation(summary = "Deletar tarefas concluídas")
    public void deleteCompletedTasks() {
        taskService.deleteCompletedTasks();// Função para deletar todas as tarefas completadas;
    }
}