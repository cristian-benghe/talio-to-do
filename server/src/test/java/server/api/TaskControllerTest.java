package server.api;

import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testGetAll() {
        // given
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Task 1", false));
        tasks.add(new Task(2, "Task 2", false));
        when(taskService.getAll()).thenReturn(tasks);

        // when
        List<Task> result = taskController.getAll();

        // then
        assertEquals(tasks, result);
        verify(taskService, times(1)).getAll();
    }*/

    @Test
    void testGetByID() {
        // given
        Task task = new Task(1, "Task 1", false);
        when(taskService.getByID(anyLong())).thenReturn(Optional.of(task));

        // when
        ResponseEntity<Task> response = taskController.getByID(1);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).getByID(1);
    }

    @Test
    void testGetByIDNotFound() {
        // given
        when(taskService.getByID(anyLong())).thenReturn(Optional.empty());

        // when
        ResponseEntity<Task> response = taskController.getByID(1);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskService, times(1)).getByID(1);
    }

    @Test
    void testAdd() {
        // given
        Task task = new Task(1, "Task 1", false);
        when(taskService.add(any(Task.class))).thenReturn(task);

        // when
        ResponseEntity response = taskController.add(task);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).add(task);
    }

    /*@Test
    void testDelete() {
        // given
        long id = 1;

        // when
        ResponseEntity<Task> response = taskController.delete(id);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).delete(id);
    }*/

}