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
        task1 = new Task(1L, "Task 1", false, Task.Priority.ALTA, LocalDate.now(), "Prevista");
        task2 = new Task(2L, "Task 2", false, Task.Priority.MEDIA, LocalDate.now(), "Prevista");
        completedTask = new Task(3L, "Completed Task", true, Task.Priority.BAIXA, LocalDate.now(), "Concluída");
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should add a new task")
    public void should_add_task() {
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        ResponseEntity<Task> response = taskService.addTask(task1);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(task1, response.getBody());
    }

    @Test
    @DisplayName("Should list all tasks")
    public void should_list_all_tasks() {
        List<Task> taskList = List.of(task1, task2);
        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> result = taskService.listAllTasks();

        assertEquals(taskList.size(), result.size());
        assertEquals(taskList, result);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should complete a task")
    public void should_complete_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        ResponseEntity<Void> response = taskService.completeTask(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertTrue(task1.isCompleted());
    }

    /*@SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should update a task")
    public void should_update_task() {
        // Create an updated task with completed status
        Task updatedTask = new Task(1L, "Updated Task", true, Task.Priority.BAIXA, LocalDate.now(), "Concluída");
        
        // Mock the behavior of the task repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        // Call the updateTask method
        ResponseEntity<Task> response = taskService.updateTask(1L, updatedTask);
    
        // Verify the response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    
        // Retrieve the updated task from the repository
        Task retrievedTask = taskRepository.findById(1L).orElse(null);
        assertNotNull(retrievedTask);
    
        // Compare the fields of the updated task with the retrieved task
        assertEquals(updatedTask.getDescription(), retrievedTask.getDescription());
        assertEquals(updatedTask.isCompleted(), retrievedTask.isCompleted());
        assertEquals(updatedTask.getPriority(), retrievedTask.getPriority());
        assertEquals(updatedTask.getCreationDate(), retrievedTask.getCreationDate());
        assertEquals(updatedTask.getStatus(), retrievedTask.getStatus());
    }*/

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should delete a task")
    public void should_delete_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        ResponseEntity<Void> response = taskService.deleteTask(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(taskRepository, times(1)).delete(task1);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should delete completed tasks")
    public void should_delete_completed_tasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);
        allTasks.add(completedTask);

        when(taskRepository.findAll()).thenReturn(allTasks);

        ResponseEntity<Void> response = taskService.deleteCompletedTasks();

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(taskRepository, times(1)).delete(completedTask);
    }
}