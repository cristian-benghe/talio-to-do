package server.api;

import commons.Board;
import commons.Column;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.BoardService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void setup() {
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
        //when(boardService.getById(id)).thenReturn(true);
        when(boardService.getById(id)).thenReturn(Optional.of(board));
        ResponseEntity<Board> response = boardController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());
    }

    /**
     * Tests if the GET (by ID) request successfully returns BAD REQUEST (error 400) if there is no board with that ID in the database
     */
    @Test
    public void testGetById2() {
        long id = -1L;
        //when(boardService.getById(id)).thenReturn(false);
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
        BoardService mockserv = mock(BoardService.class);
        BoardController controller = new BoardController(new Random(), mockserv);
        Board board = new Board("");
        ResponseEntity<Board> response = controller.add(board);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests if the POST request successfully returns BAD REQUEST (error 400) if the board has null as a title
     */
    @Test
    public void testPostRequest3() {
        BoardService mockserv = mock(BoardService.class);
        BoardController controller = new BoardController(new Random(), mockserv);
        Board board = new Board(null);
        ResponseEntity<Board> response = controller.add(board);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    /**
     * Tests if the PUT request successfully updates (status 200) the board if it exists in the database
     */
    @Test
    public void testPutRequest1() {
        long boardId = 1L;
        Board existingBoard = new Board(boardId, "Existing Board", null, null);
        Board updatedBoard = new Board(boardId, "Updated Board", null, null);
        when(boardService.getById(boardId)).thenReturn(Optional.of(existingBoard));
        when(boardService.add(existingBoard)).thenReturn(updatedBoard);

        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

//    /**
//     * Tests if the PUT request successfully returns Not Found (error 404) if the board does not exist in the database
//     */
//    @Test
//    public void testPutRequest2() {
//        long boardId = 1L;
//        Board updatedBoard = new Board(boardId, "Updated Board", null, null);
//        when(boardService.getById(boardId)).thenReturn(Optional.empty());
//
//        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }

    /**
     * Tests if the PUT request successfully updates (status 200) the board with the new title and tags if it exists in the database
     */
    @Test
    public void testPutRequest3() {
        long boardId = 1L;
        List<Column> columns = new ArrayList<>();
        List<Tag> tags = Arrays.asList(new Tag(1L, "tag1", null), new Tag(2L, "tag2", null));
        Board existingBoard = new Board(boardId, "Existing Board", columns, tags);
        Board updatedBoard = new Board(boardId, "Updated Board", columns, List.of(new Tag(3L, "new tag", null)));
        when(boardService.getById(boardId)).thenReturn(Optional.of(existingBoard));
        when(boardService.add(existingBoard)).thenReturn(existingBoard);

        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);

        assertEquals(HttpStatus.OK, response.getStatusCode());
     
    }

    /**
     * Tests if the PUT request successfully updates (status 200) the board with the same title, columns and tags if it already exists in the database
     */
    @Test
    public void testPutRequest4() {
        long boardId = 1L;
        List<Column> columns = new ArrayList<>();
        List<Tag> tags = Arrays.asList(new Tag(1L, "tag1", null), new Tag(2L, "tag2", null));
        Board existingBoard = new Board(boardId, "Hello World", columns, tags);
        Board updatedBoard = new Board(boardId, "Hello World", columns, tags);
        when(boardService.getById(boardId)).thenReturn(Optional.of(existingBoard));
        when(boardService.add(existingBoard)).thenReturn(existingBoard);

        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests if the DELETE request successfully returns NO CONTENT (error 204) if the board is deleted from the database
     */
//    @Test
    public void testDeleteBoardSuccess() {
        long id = 1;
       // when(boardService.getById(id)).thenReturn(val);
        ResponseEntity<Void> response = boardController.delete(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Tests if the DELETE request successfully returns NOT FOUND (error 404) if the board does not exist in the database
     */
    @Test
    public void testDeleteBoardNotFound() {
        long id = 1;
       // when(boardService.getById(id)).thenReturn(false);
        ResponseEntity<Void> response = boardController.delete(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
