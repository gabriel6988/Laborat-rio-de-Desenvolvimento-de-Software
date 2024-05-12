package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Roteiro01Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + restTemplate.getRootUri().split(":")[2] + "/tasks";
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
        /*TaskData taskData;
        taskData = new TaskData();
        taskData.setId(1L);
        taskData.setDescription("Exemplo 1");
        taskData.setCompleted(false);
        taskData.setPriority(Task.Priority.ALTA);
        taskData.setDueDate(LocalDate.now().plusDays(7));

        ResponseEntity<TaskData> response = restTemplate.postForEntity(baseUrl, taskData, TaskData.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskData createdTask = response.getBody();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getDescription()).isEqualTo("Nova Tarefa");*/
    }

    @Test
    public void testGetAllTasks() {
        ResponseEntity<List<Task>> response = restTemplate.exchange(
                "/tasks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Task> tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isGreaterThan(0);

        MediaType contentType = response.getHeaders().getContentType();
        assertThat(contentType).isNotNull();
        assertThat(contentType.toString()).isEqualTo("application/json");
    }

    @Test
    public void testCompleteTask() {
        long taskId = 1L;
        ResponseEntity<?> responseEntity = restTemplate.patchForObject(
                baseUrl + "/" + taskId + "/completed", null,
                ResponseEntity.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
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
        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setCompleted(true);

        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setCompleted(true);

        restTemplate.delete(baseUrl + "/completed");

        ResponseEntity<List<Task>> response = restTemplate.exchange(
                baseUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Task> tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks).doesNotContain(task1);
        assertThat(tasks).doesNotContain(task2);
    }
}