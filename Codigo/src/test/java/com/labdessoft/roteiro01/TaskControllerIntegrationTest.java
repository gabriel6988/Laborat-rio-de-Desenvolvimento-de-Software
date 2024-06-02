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
import java.time.LocalDate;
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

    /*@Test
    public void testCreateTask() {
        Task task = new Task();
        task.setDescription("Nova Tarefa");
        task.setCompleted(false);
        task.setPriority(Task.Priority.ALTA);
        task.setCreationDate(LocalDate.now());
        task.setStatus("Prevista");

        given()
            .contentType(ContentType.JSON)
            .body(task)
            .when()
            .post("/api/tasks/")
            .then()
            .statusCode(201); // Tarefa criada com sucesso;
    }*/

    @Test
    public void testGetAllTasks() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks/")
                .then()
                .statusCode(200) // Lista de tarefas retornada com sucesso;
                .contentType(ContentType.JSON);
    }

    @Test
    public void testCompleteTask() {
        long taskId = 1L;
        given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/tasks/{taskId}/completed", taskId)
                .then()
                .statusCode(204); // Tarefa marcada como concluída;
    }

    /*@Test
    public void testUpdateTask() {
        // Cria uma nova tarefa.
        Task newTask = new Task();
        newTask.setDescription("Nova Tarefa");
        newTask.setPriority(Task.Priority.ALTA);
        newTask.setCompleted(false);
        newTask.setCreationDate(LocalDate.now());
        newTask.setStatus("Prevista");

        // Criar a tarefa via solicitação POST;
        Task createdTask = given()
            .contentType(ContentType.JSON)
            .body(newTask)
            .when()
            .post("/api/tasks/")
            .then()
            .statusCode(201)
            .extract().body().as(Task.class);

        long taskId = createdTask.getId(); // ID da tarefa criada;

        // Atualizar a tarefa;
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setDescription("Tarefa Atualizada");
        updatedTask.setPriority(Task.Priority.BAIXA);
        updatedTask.setCompleted(false);
        updatedTask.setCreationDate(createdTask.getCreationDate());
        updatedTask.setStatus("Atualizada");

        given()
            .contentType(ContentType.JSON)
            .body(updatedTask)
            .when()
            .put("/api/tasks/")
            .then()
            .statusCode(200);

        // Verifica se a tarefa foi atualizada;
        Task retrievedTask = given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/tasks/{taskId}", taskId)
            .then()
            .statusCode(200)
            .extract().body().as(Task.class);

        assertThat(retrievedTask).isNotNull();
        assertThat(retrievedTask.getDescription()).isEqualTo("Tarefa Atualizada");
        assertThat(retrievedTask.getPriority()).isEqualTo(Task.Priority.BAIXA);
        assertThat(retrievedTask.getStatus()).isEqualTo("Atualizada");
    }*/

    @Test
    public void testDeleteTask() {
        long taskId = 1L;
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/tasks/{taskId}", taskId)
                .then()
                .statusCode(204); // Tarefa excluída com sucesso;
    }

    @Test
    public void testDeleteCompletedTasks() {
        // Criação da primeira tarefa concluída;
        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setCompleted(true);

        // Criação da segunda tarefa concluída;
        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setCompleted(true);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/tasks/completed")
                .then()
                .statusCode(204); // Tarefas concluídas excluídas com sucesso;

        // Obtêm a lista de todas as tarefas;
        List<Task> tasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks/")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", Task.class);

        assertThat(tasks).isNotNull(); // Verifica se a lista de tarefas não é nula;
        // Verifica se a lista de tarefas não contém task1 e task2, ou seja, se foram deletadas corretamente;
        assertThat(tasks).doesNotContain(task1, task2);
    }
}