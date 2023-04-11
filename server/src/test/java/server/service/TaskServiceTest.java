package server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import commons.Card;
import commons.Task;
import server.database.CardRepository;
import server.database.TaskRepository;

public class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, cardRepository);
    }

    @Test
    void testGetAll() {
        List<Task> tasks = List.of(new Task(0, "task title", false),
                new Task(0, "task title1", false));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAll();
        assertEquals(2, result.size());
        assertEquals(tasks.get(0), result.get(0));
        assertEquals(tasks.get(1), result.get(1));
    }

    @Test
    void testGetByID() {
        long id = 1L;
        Task task = new Task(0, "task title", false);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getByID(id);
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void testAdd() {
        Task task = new Task(0, "task title", false);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.add(task);
        assertEquals(task, result);
    }

    @Test
    void testAddWithNullTitle() {
        Task task = new Task(0, "", false);

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.add(task);
        });
    }

    @Test
    void testDelete() {
        long id = 1L;

        assertTrue(taskService.delete(id));
        verify(taskRepository).deleteById(id);
    }

    @Test
    void testAddTaskToCard() {
        Task task = new Task(0, "task title", false);
        long cardId = 1L;
        Card card = new Card("", "desc1", new ArrayList<>(), new HashSet<>());
        card.setId(cardId);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(taskRepository.save(task)).thenReturn(task);
        when(cardRepository.save(card)).thenReturn(card);

        Task result = taskService.addTaskToCard(task, cardId);
        assertEquals(task, result);
        assertTrue(card.getTaskList().contains(task));
    }

    @Test
    void testAddTaskToCardWithNullTask() {
        long cardId = 1L;

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.addTaskToCard(null, cardId);
        });
    }

    @Test
    void testAddTaskToCardWithNonExistentCard() {
        Task task = new Task(0, "task title", false);
        long cardId = 1L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.addTaskToCard(task, cardId);
        });
    }

    @Test
    void testUpdateTask() {
        Task task = new Task(0, "task title", false);
        task.setID(1L);
        when(taskRepository.findById(task.getID())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = new Task(0, "task title", false);
        updatedTask.setID(1L);
        updatedTask.setStatus(true);

        Task result = taskService.updateTask(updatedTask);
        assertEquals(updatedTask,result);
    }
}
