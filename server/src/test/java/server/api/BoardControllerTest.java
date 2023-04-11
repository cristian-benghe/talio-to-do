package server.api;

import commons.Board;
import commons.Column;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.BoardService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        boardService = mock(BoardService.class);
        boardController = new BoardController(null, boardService);
    }

    /**
     * Tests if the GET request successfully returns all the boards in the database
     */
    @Test
    public void testGetAll() {
        List<Board> boards = new ArrayList<>();
        boards.add(new Board("Board 1"));
        boards.add(new Board("Board 2"));
        boards.add(new Board("Board 3"));

        when(boardService.getAll()).thenReturn(boards);

        List<Board> actualBoards = boardController.getAll();

        assertEquals(boards.size(), actualBoards.size());

        for (int i = 0; i < boards.size(); i++) {
            Board expectedBoard = boards.get(i);
            Board actualBoard = actualBoards.get(i);

            assertEquals(expectedBoard.getId(), actualBoard.getId());
            assertEquals(expectedBoard.getTitle(), actualBoard.getTitle());
        }
    }

    /**
     * Tests if the GET (by ID) request successfully returns OK (status 200) if there is a board with that ID in the database
     */
    @Test
    public void testGetById_WhenIdIsValid_ReturnsBoard() {
        long id = 1L;
        Board board = new Board(id, "Hello World", null, null);
        when(boardService.getById(id)).thenReturn(Optional.of(board));
        ResponseEntity<Board> response = boardController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());
    }

    /**
     * Tests if the GET (by ID) request successfully returns NOT FOUND (error 404) if there is no board with that ID in the database
     */
    @Test
    public void testGetById_WhenIdIsInvalid_ReturnsNotFound() {
        long id = -1L;
        when(boardService.getById(id)).thenReturn(Optional.empty());
        ResponseEntity<Board> response = boardController.getById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests if the POST request successfully adds a board to the database (status 200)
     */
    @Test
    public void testAddSuccess() {
        Board board = new Board(1, "Hello World", null, null);
        when(boardService.add(any(Board.class))).thenReturn(board);
        ResponseEntity<Board> response = boardController.add(board);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests if the POST request successfully returns BAD REQUEST (error 400) if the board has an empty value as a title
     */
    @Test
    public void testAddBoardWithEmptyTitle() {
        Board board = new Board("");
        ResponseEntity<Board> response = boardController.add(board);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}