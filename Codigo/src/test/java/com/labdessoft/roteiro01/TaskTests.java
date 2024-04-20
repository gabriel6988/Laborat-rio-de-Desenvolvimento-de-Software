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
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.mockito.ArgumentCaptor;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskTests {

    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskRepository taskRepository;
    private TaskData taskData;
    private TaskPrazo taskPrazo;
    private TaskLivre taskLivre;

    @BeforeAll
    public void setUp() {
        // Cria uma instância de cada subclasse de Task;
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

    // Teste para adicionar uma task "DATA" na lista;
    @Test
    public void testAddTaskDATA() throws Exception {
        when(taskRepository.save(any(Task.class))).thenReturn(taskData);

        ResponseEntity<String> responseEntity = taskController.addTask(taskData);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());

        verify(taskRepository, times(1)).save(taskData);
    }

    // Teste para adicionar uma task "PRAZO" na lista;
    @Test
    public void testAddTaskPRAZO() throws Exception {
        when(taskRepository.save(any(Task.class))).thenReturn(taskPrazo);

        ResponseEntity<String> responseEntity = taskController.addTask(taskPrazo);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());

        verify(taskRepository, times(1)).save(taskPrazo);
    }

    // Teste para adicionar uma task "LIVRE" na lista;
    @Test
    public void testAddTaskLIVRE() throws Exception {
        when(taskRepository.save(any(Task.class))).thenReturn(taskLivre);

        ResponseEntity<String> responseEntity = taskController.addTask(taskLivre);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Tarefa adicionada com sucesso.", responseEntity.getBody());

        verify(taskRepository, times(1)).save(taskLivre);
    }

    // Teste para verificar o READ, obter uma lista com todas as tarefas;
    @Test
    public void testListAllTasks() throws Exception {
        when(taskRepository.findAll()).thenReturn(List.of(taskData, taskPrazo, taskLivre));
        assertThat(taskController.listAllTasks()).contains(taskData, taskPrazo, taskLivre);
    }

    // Teste de um UPDATE de uma task;
    @Test
    public void testUpdateTask() throws Exception {
        // Criando uma nova tarefa e a sua versão atualizada;
        TaskPrazo existingTask;
        existingTask = new TaskPrazo();
        existingTask.setId(4L);
        existingTask.setDescription("Existing Task");
        existingTask.setCompleted(false);
        existingTask.setPriority(Task.Priority.MEDIA);
        existingTask.setDeadlineInDays(10);

        TaskPrazo updatedTask;
        updatedTask = new TaskPrazo();
        updatedTask.setId(4L);
        updatedTask.setDescription("Updated Task");
        updatedTask.setCompleted(false);
        updatedTask.setPriority(Task.Priority.ALTA);
        updatedTask.setDeadlineInDays(8);

        when(taskRepository.findById(4L)).thenReturn(Optional.of(existingTask));

        // Invocando o método updateTask;
        taskController.updateTask(4L, updatedTask);

        // Captor para capturar a tarefa salva;
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());
        Task savedTask = captor.getValue();

        if (savedTask instanceof TaskPrazo savedTaskPrazo) {
            // Verifique se a tarefa salva corresponde à tarefa atualizada;
            assertEquals(updatedTask.getId(), savedTask.getId());
            assertEquals(updatedTask.getDescription(), savedTask.getDescription());
            assertEquals(updatedTask.getPriority(), savedTask.getPriority());
            assertEquals(updatedTask.getDeadlineInDays(), savedTaskPrazo.getDeadlineInDays());
        } else {
            // Caso a task salva não seja igual a task atualizada;
            throw new AssertionError("A tarefa salva não é uma instância TaskPrazo");
        }
    }

    // Teste para fazer um update para completar a task.
    @Test
    public void testCompleteTask() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskData));

        taskController.completeTask(1L);// Marcar a task como completa;

        assertThat(taskData.isCompleted()).isTrue();
    }

    // Teste para deletar uma única tarefa;
    @Test
    public void testDeleteTask() throws Exception{

        // Setup e Invocação;
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskData));
        ResponseEntity<String> responseEntity = taskController.deleteTask(1L);

        // Verificação;
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Tarefa deletada com sucesso.");
        verify(taskRepository, times(1)).delete(taskData);
    }

    // Teste para deletar tarefas concluídas;
    @Test
    public void testDeleteCompletedTasks() throws Exception {
        // Criando novas Tarefas;
        TaskLivre completedTask;
        completedTask = new TaskLivre();
        completedTask.setId(4L);
        completedTask.setDescription("Completed Task");
        completedTask.setCompleted(true);
        completedTask.setPriority(Task.Priority.BAIXA);

        TaskLivre uncompletedTask;
        uncompletedTask = new TaskLivre();
        uncompletedTask.setId(5L);
        uncompletedTask.setDescription("Uncompleted Task");
        uncompletedTask.setCompleted(false);
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