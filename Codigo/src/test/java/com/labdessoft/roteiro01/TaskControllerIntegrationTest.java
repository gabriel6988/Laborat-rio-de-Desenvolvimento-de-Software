package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Roteiro01Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseUrl = "http://localhost:" + port + "/tasks";
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setDescription("Nova Tarefa");
        task.setCompleted(false);
        task.setPriority(Task.Priority.ALTA);

        ResponseEntity<Task> response = restTemplate.postForEntity(baseUrl, task, Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task createdTask = response.getBody();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getDescription()).isEqualTo("Nova Tarefa");
    }

    @Test
    public void testGetAllTasks() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Task> tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isGreaterThan(0);
    }

    @Test
    public void testCompleteTask() {
        long taskId = 1L;
        ResponseEntity<Void> response = restTemplate.patchForObject(baseUrl + "/" + taskId + "/completed", null, ResponseEntity.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testUpdateTask() {
        long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setDescription("Tarefa Atualizada");

        restTemplate.put(baseUrl + "/" + taskId, updatedTask);

        ResponseEntity<Task> response = restTemplate.getForEntity(baseUrl + "/" + taskId, Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask = response.getBody();
        assertThat(retrievedTask).isNotNull();
        assertThat(retrievedTask.getDescription()).isEqualTo("Tarefa Atualizada");
    }

    @Test
    public void testDeleteTask() {
        long taskId = 1L;
        restTemplate.delete(baseUrl + "/" + taskId);

        ResponseEntity<Task> response = restTemplate.getForEntity(baseUrl + "/" + taskId, Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCompletedTasks() {
        // Crie e conclua algumas tarefas;
        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setCompleted(true);

        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setCompleted(true);

        // Chama o endpoint deleteCompletedTasks;
        restTemplate.delete(baseUrl + "/completed");

        // Recuperar todas as tarefas e verificar se as tarefas concluídas foram excluídas;
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Task> tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks).doesNotContain(task1);
        assertThat(tasks).doesNotContain(task2);
    }
}