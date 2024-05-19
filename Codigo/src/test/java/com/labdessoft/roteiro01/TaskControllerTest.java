package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController taskController;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Task 1", false, Task.Priority.ALTA, null, null));
        tasks.add(new Task(2, "Task 2", true, Task.Priority.MEDIA, null, null));

        when(taskService.listAllTasks()).thenReturn(tasks);

        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }
}