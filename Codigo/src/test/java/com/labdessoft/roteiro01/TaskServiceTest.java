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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private List<Task> tasks;

    @BeforeEach
    public void setup() {
        // Crie uma lista de tarefas simulada para uso nos testes;
        tasks = List.of(
            new Task(1L, "Task 1", false, Task.Priority.ALTA, LocalDate.now(), "Prevista"),
            new Task(2L, "Task 2", true, Task.Priority.MEDIA, LocalDate.now(), "Concluída")
        );
    }

    @Test
    @DisplayName("Should return all tasks")
    public void should_list_all_tasks() {
        // Configurar o comportamento do repositório simulado;
        when(taskRepository.findAll()).thenReturn(tasks);
        
        // Executar o método a ser testado;
        List<Task> result = taskService.listAllTasks();

        // Verificar os resultados;
        assertEquals(2, result.size());
        assertEquals(tasks, result);
    }
}