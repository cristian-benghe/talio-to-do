package server.service;

import commons.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(null, "Task 1", false));
        tasks.add(new Task(null, "Task 2", false));

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> allTasks = taskService.getAll();

        Assertions.assertEquals(2, allTasks.size());
        Assertions.assertEquals("Task 1", allTasks.get(0).getTitle());
        Assertions.assertEquals("Task 2", allTasks.get(1).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getByID() {
        Task task = new Task(null, "Task 1", false);
        task.setID(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.getByID(1L);

        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals("Task 1", foundTask.get().getTitle());
        Assertions.assertEquals(1L, foundTask.get().getID());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void add() {
        Task task = new Task(null, "Task 1", false);

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.add(task);

        Assertions.assertEquals(task.getTitle(), savedTask.getTitle());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void addWithNullTitle() {
        Task task = new Task(null, null, false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.add(task));

        verify(taskRepository, never()).save(task);
    }

    @Test
    void addWithEmptyTitle() {
        Task task = new Task(null, "", false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.add(task));

        verify(taskRepository, never()).save(task);
    }

    @Test
    void delete() {
        long taskId = 1L;

        doNothing().when(taskRepository).deleteById(taskId);

        taskService.delete(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

}