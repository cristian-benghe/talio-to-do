package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import server.api.BoardController;
import server.database.BoardRepository;

public class BoardControllerTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteBoardSuccess() {
        long id = 1;
        when(boardRepository.existsById(id)).thenReturn(true);
        ResponseEntity<Void> response = boardController.delete(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteBoardNotFound() {
        long id = 1;
        when(boardRepository.existsById(id)).thenReturn(false);
        ResponseEntity<Void> response = boardController.delete(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
