package server.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import commons.Board;
import commons.Card;
import commons.Column;
import server.database.CardRepository;
import server.database.ColumnRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ColumnControllerTest {

    @Mock
    private ColumnRepository repo;

    @Mock
    private CardRepository cardrepo;

    @InjectMocks
    private ColumnController controller;

    private List<Column> columns;

    @BeforeEach
    public void setUp() {
        columns = new ArrayList<>();

        Column c1 = new Column("Column 1", new ArrayList<Card>(), new Board("Board 1"));
        c1.setId(1L);

        Column c2 = new Column("Column 2", new ArrayList<Card>(), new Board("Board 1"));
        c2.setId(2L);

        columns.add(c1);
        columns.add(c2);
    }

    @Test
    public void testGetAllColumns() {
        when(repo.findAll()).thenReturn(columns);

        List<Column> result = controller.getAll();

        assertEquals(columns, result);
        assertEquals(columns.size(), result.size());

        verify(repo, times(1)).findAll();
    }

    @Test
    void testGetById() {
        // Given
        long id = 1L;
        Column column = new Column("Column 1", new ArrayList<Card>(), new Board("Board 1"));
        when(repo.findById(id)).thenReturn(Optional.of(column));

        // When
        ResponseEntity<Column> response = controller.getById(id);

        // Then
        assertEquals(column, response.getBody());
    }

    @Test
    void testGetByIdWithInvalidId() {
        // Given
        long id = -1L;

        // When
        ResponseEntity<Column> response = controller.getById(id);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAdd() {
        // Given
        Column column = new Column("Column 1", new ArrayList<Card>(), new Board("Board 1"));
        when(repo.save(column)).thenReturn(column);

        // When
        ResponseEntity<Column> response = controller.add(column);

        // Then
        assertEquals(column, response.getBody());
    }

    @Test
    void testAddWithNullTitle() {
        // Given
        Column column = new Column(null, new ArrayList<Card>(), new Board("Board 1"));

        // When
        ResponseEntity<Column> response = controller.add(column);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate() {
        // Given
        long id = 1L;
        Column existing = new Column("Column 1", new ArrayList<Card>(), new Board("Board 1"));
        Column updated = new Column("Updated Column", new ArrayList<Card>(), new Board("Board 1"));
        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        // When
        ResponseEntity<Column> response = controller.update(id, updated);

        // Then
        assertEquals(existing, response.getBody());
        assertEquals("Updated Column", existing.getTitle());
    }

}