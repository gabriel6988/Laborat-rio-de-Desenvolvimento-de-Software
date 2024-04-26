
package com.labdessoft.roteiro01.service;

import com.labdessoft.roteiro01.entity.*;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Função para adicionar a tarefa na lista;
    public String addTask(Task task) {
        if (task instanceof TaskData && LocalDate.now().isAfter(((TaskData) task).getDueDate())) {
            return "A data prevista deve ser igual ou superior à data atual.";
        } else if (task instanceof TaskPrazo && LocalDate.now().isAfter(((TaskPrazo) task).getCreationDate().plusDays(((TaskPrazo) task).getDeadlineInDays()))) {
            return "O prazo previsto para a conclusão deve ser igual ou superior à data atual.";
        }
        taskRepository.save(task);
        return "Tarefa adicionada com sucesso.";
    }

    // Função para listar todas as tarefas da lista;
    public List<Task> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            task.setStatus(getTaskStatus(task));
        }
        return tasks;
    }

    // Função que pega os status das tarefas (Concluída, Prevista ou Atrasada);
    private String getTaskStatus(Task task) {
        if (task instanceof TaskData taskData) {
            LocalDate currentDate = LocalDate.now();
            if (taskData.isCompleted()) {
                return "Concluída";
            } else if (currentDate.isAfter(taskData.getDueDate())) {
                return ChronoUnit.DAYS.between(taskData.getDueDate(), currentDate) + " dias de atraso";
            } else {
                return "Prevista";
            }
        } else if (task instanceof TaskPrazo taskPrazo) {
            LocalDate currentDate = LocalDate.now();
            LocalDate predictedDate = taskPrazo.getCreationDate().plusDays(taskPrazo.getDeadlineInDays());
            if (taskPrazo.isCompleted()) {
                return "Concluída";
            } else if (currentDate.isAfter(predictedDate)) {
                return ChronoUnit.DAYS.between(predictedDate, currentDate) + " dias de atraso";
            } else {
                return "Prevista";
            }
        } else if (task instanceof TaskLivre) {
            return task.isCompleted() ? "Concluída" : "Prevista";
        } else {
            return "Status não definido";
        }
    }

    // Função que marca a tarefa como completada;
    public void completeTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setCompleted(true);
            taskRepository.save(task);
        }
    }

    // Função que altera/edita a tarefa;
    public void updateTask(long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            if (updatedTask instanceof TaskData updatedTaskData && task instanceof TaskData) {
                ((TaskData) task).setDueDate(updatedTaskData.getDueDate());
            }
            else if (updatedTask instanceof TaskPrazo updatedTaskPrazo && task instanceof TaskPrazo) {
                ((TaskPrazo) task).setDeadlineInDays(updatedTaskPrazo.getDeadlineInDays());
            }
            task.setDescription(updatedTask.getDescription());
            task.setPriority(updatedTask.getPriority());
            taskRepository.save(task);
        }
    }

    // Função que deleta uma tarefa;
    public String deleteTask(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
            return "Tarefa deletada com sucesso.";
        } else {
            return "Tarefa não encontrada.";
        }
    }

    // Função que deleta todas as tarefas completadas;
    public void deleteCompletedTasks() {
        List<Task> allTasks = taskRepository.findAll();
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                taskRepository.delete(task);
            }
        }
    }
}