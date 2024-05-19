/*import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testFindAll() {
        // Create some sample tasks
        Task task1 = new Task(1L, "Task 1", false, Task.Priority.ALTA, null, null);
        Task task2 = new Task(2L, "Task 2", true, Task.Priority.MEDIA, null, null);

        // Save the tasks to the database
        taskRepository.saveAll(List.of(task1, task2));

        // Call the repository method
        List<Task> tasks = taskRepository.findAll();

        // Assert that the tasks are retrieved correctly
        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    void testFindById() {
        // Create a sample task
        Task task = new Task(1L, "Task 1", false, Task.Priority.ALTA, null, null);

        // Save the task to the database
        taskRepository.save(task);

        // Call the repository method
        Optional<Task> optionalTask = taskRepository.findById(1L);

        // Assert that the task is retrieved correctly
        assertEquals(task, optionalTask.orElse(null));
    }
}*/