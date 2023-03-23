package server.api;

//import commons.Board;
import commons.Card;
import commons.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.CardRepository;
import server.database.ColumnRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

        Column c1 = new Column("Column 1", new ArrayList<Card>());
        c1.setId(1L);

        Column c2 = new Column("Column 2", new ArrayList<Card>());
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
        Column column = new Column("Column 1", new ArrayList<Card>());
        when(repo.findById(id)).thenReturn(Optional.of(column));
        when(repo.existsById(id)).thenReturn(true);

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
        Column column = new Column("Column 1", new ArrayList<Card>());
        when(repo.save(column)).thenReturn(column);

        // When
        ResponseEntity<Column> response = controller.add(column);

        // Then
        assertEquals(column, response.getBody());
    }

    @Test
    void testAddWithNullTitle() {
        // Given
        Column column = new Column(null, new ArrayList<Card>());

        // When
        ResponseEntity<Column> response = controller.add(column);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdate() {
        // Given
        long id = 1L;
        Column existing = new Column("Column 1", new ArrayList<Card>());
        Column updated = new Column("Updated Column", new ArrayList<Card>());
        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        // When
        ResponseEntity<Column> response = controller.update(id, updated);

        // Then
        assertEquals(existing, response.getBody());
        assertEquals("Updated Column", existing.getTitle());
    }

    @Test
    void testUpdateNotExistingByID() {
        // Given
        long id = -1L;
        Column updated = new Column("Column 1", new ArrayList<Card>());
        when(repo.findById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Column> response = controller.update(id, updated);

        // Then
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testDeleteNotExistingByID() {
        // Given
        long id = -1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity response = controller.delete(id);

        // Then
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testDelete() {
        // Given
        long id = 1L;
        Column existing = new Column("Column 1", new ArrayList<Card>());
        existing.setCards(List.of(new Card("card",null,null,null)));
        when(repo.findById(id)).thenReturn(Optional.of(existing));

        // When
        ResponseEntity response = controller.delete(id);

        // Then
        assertEquals(ResponseEntity.ok().build(), response);
    }
}