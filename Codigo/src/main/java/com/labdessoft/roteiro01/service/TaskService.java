package com.labdessoft.roteiro01.service;

import com.labdessoft.roteiro01.entity.*;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Adiciona uma nova tarefa.
     *
     * @param task Tarefa a ser adicionada.
     * @return ResponseEntity com a tarefa adicionada e status HTTP apropriado.
     */
    public ResponseEntity<Task> addTask(Task task) {
        // Verificação de restrições
        if (task instanceof TaskData taskData) {
            if (LocalDate.now().isAfter(taskData.getDueDate())) {
                return ResponseEntity.badRequest()
                        .body(taskData); // Retorna a tarefa com uma mensagem de erro
            }
        } else if (task instanceof TaskPrazo taskPrazo) {
            LocalDate predictedDate = taskPrazo.getCreationDate().plusDays(taskPrazo.getDeadlineInDays());
            if (LocalDate.now().isAfter(predictedDate)) {
                return ResponseEntity.badRequest()
                        .body(taskPrazo); // Retorna a tarefa com uma mensagem de erro
            }
        }

        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    /**
     * Lista todas as tarefas.
     *
     * @return ResponseEntity com a lista de tarefas e status HTTP 200 OK.
     */
    public ResponseEntity<List<Task>> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(this::setTaskStatus);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Define o status da tarefa com base em seu tipo.
     *
     * @param task Tarefa a ser verificada.
     */
    private void setTaskStatus(Task task) {
        if (task instanceof TaskData taskData) {
            LocalDate currentDate = LocalDate.now();
            if (taskData.isCompleted()) {
                taskData.setStatus("Concluída");
            } else if (currentDate.isAfter(taskData.getDueDate())) {
                long diasAtraso = ChronoUnit.DAYS.between(taskData.getDueDate(), currentDate);
                taskData.setStatus(diasAtraso + " dias de atraso");
            } else {
                taskData.setStatus("Prevista");
            }
        } else if (task instanceof TaskPrazo taskPrazo) {
            LocalDate currentDate = LocalDate.now();
            LocalDate predictedDate = taskPrazo.getCreationDate().plusDays(taskPrazo.getDeadlineInDays());
            if (taskPrazo.isCompleted()) {
                taskPrazo.setStatus("Concluída");
            } else if (currentDate.isAfter(predictedDate)) {
                long diasAtraso = ChronoUnit.DAYS.between(predictedDate, currentDate);
                taskPrazo.setStatus(diasAtraso + " dias de atraso");
            } else {
                taskPrazo.setStatus("Prevista");
            }
        } else if (task instanceof TaskLivre) {
            task.setStatus(task.isCompleted() ? "Concluída" : "Prevista");
        } else {
            task.setStatus("Status não definido");
        }
    }

    /**
     * Marca uma tarefa como concluída.
     *
     * @param id ID da tarefa a ser marcada como concluída.
     * @return ResponseEntity com status HTTP apropriado.
     */
    public ResponseEntity<Void> completeTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        task.setCompleted(true);
        taskRepository.save(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza uma tarefa existente.
     *
     * @param id ID da tarefa a ser atualizada.
     * @param updatedTask Tarefa atualizada.
     * @return ResponseEntity com a tarefa atualizada e status HTTP 200 OK.
     */
    public ResponseEntity<Task> updateTask(long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualizar os campos da tarefa com os valores da tarefa atualizada
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());

        // Atualização específica para os tipos de tarefa TaskData e TaskPrazo
        if (updatedTask instanceof TaskData updatedTaskData && task instanceof TaskData) {
            ((TaskData) task).setDueDate(updatedTaskData.getDueDate());
        } else if (updatedTask instanceof TaskPrazo updatedTaskPrazo && task instanceof TaskPrazo) {
            ((TaskPrazo) task).setDeadlineInDays(updatedTaskPrazo.getDeadlineInDays());
        }

        // Salvar a tarefa atualizada
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    /**
     * Exclui uma tarefa específica.
     *
     * @param id ID da tarefa a ser excluída.
     * @return ResponseEntity com status HTTP apropriado.
     */
    public ResponseEntity<Void> deleteTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exclui todas as tarefas concluídas.
     *
     * @return ResponseEntity com status HTTP apropriado.
     */
    public ResponseEntity<Void> deleteCompletedTasks() {
        List<Task> allTasks = taskRepository.findAll();
        allTasks.stream()
                .filter(Task::isCompleted)
                .forEach(taskRepository::delete);
        return ResponseEntity.noContent().build();
    }
}