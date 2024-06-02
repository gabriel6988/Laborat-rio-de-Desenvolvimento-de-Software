package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskData;
import com.labdessoft.roteiro01.entity.TaskLivre;
import com.labdessoft.roteiro01.entity.TaskPrazo;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskControllerTest {
    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskRepository taskRepository;

    private TaskData taskData;
    private TaskPrazo taskPrazo;
    private TaskLivre taskLivre;

    @BeforeAll
    public void setUp() {
        // Criando instâncias de cada subclasse de Task;
        taskData = new TaskData();
        taskData.setId(1L);
        taskData.setDescription("Exemplo 1");
        taskData.setCompleted(false);
        taskData.setPriority(Task.Priority.ALTA);
        taskData.setDueDate(LocalDate.now().plusDays(7));

        taskPrazo = new TaskPrazo();
        taskPrazo.setId(2L);
        taskPrazo.setDescription("Exemplo 2");
        taskPrazo.setCompleted(false);
        taskPrazo.setPriority(Task.Priority.MEDIA);
        taskPrazo.setDeadlineInDays(10);

        taskLivre = new TaskLivre();
        taskLivre.setId(3L);
        taskLivre.setDescription("Exemplo 3");
        taskLivre.setCompleted(false);
        taskLivre.setPriority(Task.Priority.BAIXA);
    }

    // Teste para adicionar uma tarefa TaskData;
    @Test
    public void testAddTaskData() {
        when(taskRepository.save(any(Task.class))).thenReturn(taskData);

        ResponseEntity<Task> response = taskController.createTask(taskData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(taskData);

        verify(taskRepository, times(1)).save(taskData);
    }

    // Teste para adicionar uma tarefa TaskPrazo;
    @Test
    public void testAddTaskPrazo() {
        when(taskRepository.save(any(Task.class))).thenReturn(taskPrazo);

        ResponseEntity<Task> response = taskController.createTask(taskPrazo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(taskPrazo);

        verify(taskRepository, times(1)).save(taskPrazo);
    }

    // Teste para adicionar uma tarefa TaskLivre;
    @Test
    public void testAddTaskLivre() {
        when(taskRepository.save(any(Task.class))).thenReturn(taskLivre);

        ResponseEntity<Task> response = taskController.createTask(taskLivre);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(taskLivre);

        verify(taskRepository, times(1)).save(taskLivre);
    }

    // Teste para verificar a lista de todas as tarefas;
    @Test
    public void testGetAllTasks() {
        List<Task> tasks = List.of(taskData, taskPrazo, taskLivre);
        when(taskRepository.findAll()).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).contains(taskData, taskPrazo, taskLivre);
    }

    // Teste para concluir uma tarefa;
    @Test
    public void testCompleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskData));

        ResponseEntity<Void> response = taskController.completeTask(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(taskData.isCompleted()).isTrue();
        verify(taskRepository, times(1)).save(taskData);
    }

    // Teste para atualizar uma tarefa;
    @Test
    public void testUpdateTask() {
        TaskPrazo existingTask = new TaskPrazo();
        existingTask.setId(4L);
        existingTask.setDescription("Tarefa existente");
        existingTask.setCompleted(false);
        existingTask.setPriority(Task.Priority.MEDIA);
        existingTask.setDeadlineInDays(10);

        TaskPrazo updatedTask = new TaskPrazo();
        updatedTask.setId(4L);
        updatedTask.setDescription("Tarefa atualizada");
        updatedTask.setCompleted(false);
        updatedTask.setPriority(Task.Priority.ALTA);
        updatedTask.setDeadlineInDays(8);

        // Configura o mock para retornar a tarefa existente;
        when(taskRepository.findById(4L)).thenReturn(Optional.of(existingTask));

        // Invoca o método de atualização;
        ResponseEntity<Task> response = taskController.updateTask(updatedTask);

        // Verifica o código de resposta HTTP;
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Captura a tarefa salva;
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());

        // Verifica se a tarefa salva é igual à tarefa atualizada;
        TaskPrazo savedTask = (TaskPrazo) captor.getValue();
        assertThat(savedTask.getId()).isEqualTo(updatedTask.getId());
        assertThat(savedTask.getDescription()).isEqualTo(updatedTask.getDescription());
        assertThat(savedTask.getPriority()).isEqualTo(updatedTask.getPriority());
        assertThat(savedTask.getDeadlineInDays()).isEqualTo(updatedTask.getDeadlineInDays());

        // Verifica o corpo da resposta;
        assertThat(response.getBody()).isEqualTo(savedTask);
    }

    // Teste para excluir uma tarefa específica;
    @Test
    public void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskData));

        ResponseEntity<Void> response = taskController.deleteTask(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskRepository, times(1)).delete(taskData);
    }

    // Teste para excluir tarefas concluídas;
    @Test
    public void testDeleteCompletedTasks() {
        // Criando tarefas concluídas e não concluídas;
        TaskLivre completedTask = new TaskLivre();
        completedTask.setId(4L);
        completedTask.setDescription("Tarefa concluída");
        completedTask.setCompleted(true);
        completedTask.setPriority(Task.Priority.BAIXA);

        TaskLivre uncompletedTask = new TaskLivre();
        uncompletedTask.setId(5L);
        uncompletedTask.setDescription("Tarefa não concluída");
        uncompletedTask.setCompleted(false);
        uncompletedTask.setPriority(Task.Priority.ALTA);

        List<Task> tasks = new ArrayList<>();
        tasks.add(completedTask);
        tasks.add(uncompletedTask);

        when(taskRepository.findAll()).thenReturn(tasks);

        ResponseEntity<Void> response = taskController.deleteCompletedTasks();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskRepository, times(1)).delete(completedTask);
        verify(taskRepository, never()).delete(uncompletedTask);
    }
}