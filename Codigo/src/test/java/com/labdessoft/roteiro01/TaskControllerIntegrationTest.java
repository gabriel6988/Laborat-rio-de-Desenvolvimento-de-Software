package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Roteiro01Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    private static final int PORT = 8080;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setDescription("Nova Tarefa");
        task.setCompleted(false);
        task.setPriority(Task.Priority.ALTA);

        given()
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);
    }

    @Test
    public void testGetAllTasks() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testCompleteTask() {
        long taskId = 1L;

        given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/tasks/{taskId}/completed", taskId)
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateTask() {
        long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setDescription("Tarefa Atualizada");

        given()
                .contentType(ContentType.JSON)
                .body(updatedTask)
                .when()
                .put("/tasks/{taskId}", taskId)
                .then()
                .statusCode(200);

        Task retrievedTask = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/{taskId}", taskId)
                .then()
                .statusCode(200)
                .extract().body().as(Task.class);

        assertThat(retrievedTask).isNotNull();
        assertThat(retrievedTask.getDescription()).isEqualTo("Tarefa Atualizada");
    }

    @Test
    public void testDeleteTask() {
        long taskId = 1L;

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/tasks/{taskId}", taskId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteCompletedTasks() {
        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setCompleted(true);

        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setCompleted(true);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/tasks/completed")
                .then()
                .statusCode(200);

        List<Task> tasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", Task.class);

        assertThat(tasks).isNotNull();
        assertThat(tasks).doesNotContain(task1, task2);
    }
}