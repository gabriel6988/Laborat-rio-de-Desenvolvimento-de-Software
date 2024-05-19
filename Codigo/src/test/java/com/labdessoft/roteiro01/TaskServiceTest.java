package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import com.labdessoft.roteiro01.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private Task completedTask;

    @BeforeEach
    public void setup() {
        // Criando instâncias de Tasks;
        task1 = new Task(1L, "Task 1", false, Task.Priority.ALTA, LocalDate.now(), "Prevista");
        task2 = new Task(2L, "Task 2", false, Task.Priority.MEDIA, LocalDate.now(), "Prevista");
        completedTask = new Task(3L, "Completed Task", true, Task.Priority.BAIXA, LocalDate.now(), "Concluída");
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Deve adicionar uma nova tarefa")
    public void should_add_task() {
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        // Chamando o método addTask do taskService com task1 como parâmetro;
        ResponseEntity<Task> response = taskService.addTask(task1);

        // Afirmando que a resposta não é nula;
        assertNotNull(response);

        // Afirmando que o código de status da resposta é 201 (CRIADO);
        assertEquals(201, response.getStatusCodeValue());

        // Afirmando que o corpo da resposta contém o objeto task1;
        assertEquals(task1, response.getBody());
    }

    @Test
    @DisplayName("Deve listar todas as tarefas")
    public void should_list_all_tasks() {
        // Criando uma lista de tarefas a serem retornadas pelo repositório simulado;
        List<Task> taskList = List.of(task1, task2);
        when(taskRepository.findAll()).thenReturn(taskList);

        // Chamando o método listAllTasks do taskService;
        List<Task> result = taskService.listAllTasks();

        // Afirmando que o tamanho da lista de resultados é igual ao tamanho da taskList;
        assertEquals(taskList.size(), result.size());

        // Afirmando que a lista de resultados é igual à taskList;
        assertEquals(taskList, result);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Deve completar uma tarefa")
    public void should_complete_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        // Chamando o método completeTask do taskService;
        ResponseEntity<Void> response = taskService.completeTask(1L);

        // Afirmando que a resposta não é nula;
        assertNotNull(response);

        // Afirmando que o código de status da resposta é 204 (SEM CONTEÚDO);
        assertEquals(204, response.getStatusCodeValue());

        // Afirmando que a task1 está marcada como concluída;
        assertTrue(task1.isCompleted());
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Deve atualizar uma tarefa")
    public void should_update_task() {
        // Tarefa original a ser atualizada;
        task1.setDescription("Old Task Description");
        task1.setPriority(Task.Priority.ALTA);
        
        // Deve atualizar uma tarefa;
        Task updatedTask = new Task(1L, "Updated Task", false, Task.Priority.BAIXA, LocalDate.now(), "Prevista");
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        // Executando a atualização;
        ResponseEntity<Task> response = taskService.updateTask(1L, updatedTask);
    
        // Afirmando a resposta;
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    
        // Recuperando a tarefa atualizada;
        Task retrievedTask = taskRepository.findById(1L).orElse(null);
        assertNotNull(retrievedTask);
    
        // Verificando as propriedades atualizadas;
        assertEquals(updatedTask.getDescription(), retrievedTask.getDescription());
        assertEquals(updatedTask.isCompleted(), retrievedTask.isCompleted());
        assertEquals(updatedTask.getPriority(), retrievedTask.getPriority());
        
        // Verifique se o método save foi chamado com a tarefa atualizada;
        verify(taskRepository).save(task1);
    
        // Garantir que a data e o status de criação não foram alterados acidentalmente;
        assertEquals(task1.getCreationDate(), retrievedTask.getCreationDate());
        assertEquals(task1.getStatus(), retrievedTask.getStatus());
    }    

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Deve excluir uma tarefa")
    public void should_delete_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        // Chamando o método deleteTask do taskService;
        ResponseEntity<Void> response = taskService.deleteTask(1L);

        // Afirmando que a resposta não é nula;
        assertNotNull(response);

        // Afirmando que o código de status da resposta é 204 (SEM CONTEÚDO);
        assertEquals(204, response.getStatusCodeValue());

        // Verificando se o método delete do repositório foi chamado exatamente uma vez com task1;
        verify(taskRepository, times(1)).delete(task1);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Deve excluir tarefas concluídas")
    public void should_delete_completed_tasks() {
        // Criação de uma lista de todas as tarefas, incluindo tarefas concluídas e incompletas;
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);
        allTasks.add(completedTask);

        when(taskRepository.findAll()).thenReturn(allTasks);

        // Chamando o método deleteCompletedTasks do taskService;
        ResponseEntity<Void> response = taskService.deleteCompletedTasks();

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(taskRepository, times(1)).delete(completedTask);
    }
}