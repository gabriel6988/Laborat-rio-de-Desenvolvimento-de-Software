package com.labdessoft.roteiro01;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.HttpStatus;


@SpringBootTest
public class TaskControllerTests {

    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskRepository taskRepository;

    // Teste para criar uma task "DATA";
    @Test
    public void testCreateTaskDATA() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");
        task.setCompleted(false);
        task.setTaskType(Task.TaskType.DATA);
        task.setPriority(Task.Priority.ALTA);
        LocalDate testDate = LocalDate.now().plusDays(5);
        task.setDueDate(testDate);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // Invocando o método addTask no controlador;
        ResponseEntity<String> responseEntity = taskController.addTask(task);
        // Verificando o status e o conteúdo da resposta;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());
        // Verificando se a tarefa está salva no repositório;
        verify(taskRepository, times(1)).save(task);
    }

    // Teste para criar uma task "PRAZO";
    @Test
    public void testCreateTaskPRAZO() throws Exception {
        Task task = new Task();
        task.setId(2L);
        task.setDescription("Test Task");
        task.setCompleted(false);
        task.setTaskType(Task.TaskType.PRAZO);
        task.setPriority(Task.Priority.MEDIA);
        task.setDeadlineInDays(9);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // Invocando o método addTask no controlador;
        ResponseEntity<String> responseEntity = taskController.addTask(task);
        // Verificando o status e o conteúdo da resposta;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());
        // Verificando se a tarefa está salva no repositório;
        verify(taskRepository, times(1)).save(task);
    }

    // Teste para criar uma task "LIVRE";
    @Test
    public void testCreateTaskLIVRE() throws Exception {
        Task task = new Task();
        task.setId(2L);
        task.setDescription("Test Task");
        task.setCompleted(false);
        task.setTaskType(Task.TaskType.LIVRE);
        task.setPriority(Task.Priority.BAIXA);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // Invocando o método addTask no controlador;
        ResponseEntity<String> responseEntity = taskController.addTask(task);
        // Verificando o status e o conteúdo da resposta;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());
        // Verificando se a tarefa está salva no repositório;
        verify(taskRepository, times(1)).save(task);
    }

    // Teste para verificar o READ, obter uma lista com todas as tarefas;
    @Test
    public void testListAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1");
        task1.setCompleted(false);
        task1.setTaskType(Task.TaskType.PRAZO);
        task1.setPriority(Task.Priority.MEDIA);
        task1.setDeadlineInDays(3);


        Task task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2");
        task2.setCompleted(true);
        task2.setTaskType(Task.TaskType.LIVRE);
        task2.setPriority(Task.Priority.BAIXA);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
        // Invocando o método listAllTasks;
        assertThat(taskController.listAllTasks()).contains(task1, task2);
    }

    // Teste de um UPDATE de uma task;
    @Test
    public void testUpdateTask() throws Exception {
        // Criar task;
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setDescription("Existing Task");
        existingTask.setCompleted(false);
        existingTask.setTaskType(Task.TaskType.LIVRE);
        existingTask.setPriority(Task.Priority.BAIXA);

        // Fazer Update;
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setDescription("Updated Task");
        updatedTask.setCompleted(true);
        updatedTask.setPriority(Task.Priority.MEDIA);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Invocando o método updateTask;
        taskController.updateTask(1L, updatedTask);

        verify(taskRepository, times(1)).save(argThat(savedTask ->
                savedTask.getId() == updatedTask.getId() &&
                        savedTask.getDescription().equals(updatedTask.getDescription()) &&
                        savedTask.isCompleted() == updatedTask.isCompleted()
        ));
    }

    // Teste para fazer um update para completar a task.
    @Test
    public void testCompleteTask() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setDescription("Existing Task");
        existingTask.setCompleted(false);
        existingTask.setTaskType(Task.TaskType.PRAZO);
        existingTask.setPriority(Task.Priority.MEDIA);
        existingTask.setDeadlineInDays(7);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskController.completeTask(1L);// Marcar a task como completa;

        assertThat(existingTask.isCompleted()).isTrue();
    }

    // Teste para deletar uma única tarefa;
    @Test
    public void testDeleteTask() throws Exception{
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setDescription("Existing Task");
        existingTask.setCompleted(false);
        existingTask.setTaskType(Task.TaskType.LIVRE);
        existingTask.setPriority(Task.Priority.MEDIA);

        // Setup e Invocação;
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        ResponseEntity<String> responseEntity = taskController.deleteTask(1L);

        // Verificação;
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Tarefa deletada com sucesso.");
        verify(taskRepository, times(1)).delete(existingTask);
    }

    // Teste para deletar tarefas concluídas;
    @Test
    public void testDeleteCompletedTasks() throws Exception {
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setDescription("Completed Task");
        completedTask.setCompleted(true);
        completedTask.setTaskType(Task.TaskType.LIVRE);
        completedTask.setPriority(Task.Priority.BAIXA);

        Task uncompletedTask = new Task();
        uncompletedTask.setId(2L);
        uncompletedTask.setDescription("Uncompleted Task");
        uncompletedTask.setCompleted(false);
        uncompletedTask.setTaskType(Task.TaskType.LIVRE);
        uncompletedTask.setPriority(Task.Priority.ALTA);

        List<Task> tasks = new ArrayList<>();
        tasks.add(completedTask);
        tasks.add(uncompletedTask);

        when(taskRepository.findAll()).thenReturn(tasks);

        taskController.deleteCompletedTasks();

        // Verifica se delete() é chamado com a tarefa concluída;
        verify(taskRepository, times(1)).delete(completedTask);
        // Verifica se delete() nunca é chamado com a tarefa incompleta;
        verify(taskRepository, never()).delete(uncompletedTask);
    }
}