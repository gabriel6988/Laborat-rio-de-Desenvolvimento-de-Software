package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Cria uma nova tarefa")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.addTask(task).getBody();
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todas as tarefas")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.listAllTasks().getBody();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Marca uma tarefa como concluída")
    @ApiResponse(responseCode = "204", description = "Tarefa marcada como concluída")
    @PatchMapping("/{id}/completed")
    public ResponseEntity<Void> completeTask(@PathVariable long id) {
        taskService.completeTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza uma tarefa existente")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        Task task = taskService.updateTask(id, updatedTask).getBody();
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Exclui uma tarefa específica")
    @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Exclui todas as tarefas concluídas")
    @ApiResponse(responseCode = "204", description = "Tarefas concluídas excluídas com sucesso")
    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedTasks() {
        taskService.deleteCompletedTasks();
        return ResponseEntity.noContent().build();
    }
}