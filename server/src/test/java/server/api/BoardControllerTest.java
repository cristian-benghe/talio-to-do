//package server.api;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import commons.Board;
//import commons.Column;
//import commons.Tag;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import server.database.BoardRepository;
//
//import java.util.*;
//
//public class BoardControllerTest {
//
//    @Mock
//    private BoardRepository boardRepository;
//
//    @InjectMocks
//    private BoardController boardController;
//
//    @BeforeEach
//    public void setup() {
//        boardRepository = mock(BoardRepository.class);
//        boardController = new BoardController(null, boardRepository);
//    }
//
//    /**
//     * Tests if the GET request successfully returns all the boards in the database
//     */
//    @Test
//    public void testGetAll() {
//        List<Board> boards = new ArrayList<>();
//        boards.add(new Board("Board 1"));
//        boards.add(new Board("Board 2"));
//        boards.add(new Board("Board 3"));
//
//        when(boardRepository.findAll()).thenReturn(boards);
//
//        List<Board> actualBoards = boardController.getAll();
//
//        assertEquals(boards.size(), actualBoards.size());
//
//        for (int i = 0; i < boards.size(); i++) {
//            Board expectedBoard = boards.get(i);
//            Board actualBoard = actualBoards.get(i);
//
//            assertEquals(expectedBoard.getId(), actualBoard.getId());
//            assertEquals(expectedBoard.getTitle(), actualBoard.getTitle());
//        }
//    }
//
//    /**
//     * Tests if the GET (by ID) request successfully returns OK (status 200) if there is a board with that ID in the database
//     */
//    @Test
//    public void testGetById_WhenIdIsValid_ReturnsBoard() {
//        long id = 1L;
//        Board board = new Board(id, "Hello World", null, null);
//        when(boardRepository.existsById(id)).thenReturn(true);
//        when(boardRepository.findById(id)).thenReturn(Optional.of(board));
//        ResponseEntity<Board> response = boardController.getById(id);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(board, response.getBody());
//    }
//
//    /**
//     * Tests if the GET (by ID) request successfully returns BAD REQUEST (error 400) if there is no board with that ID in the database
//     */
//    @Test
//    public void testGetById2() {
//        long id = -1L;
//        when(boardRepository.existsById(id)).thenReturn(false);
//        ResponseEntity<Board> response = boardController.getById(id);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    /**
//     * Tests if the POST request successfully adds a board to the database (status 200)
//     */
//    @Test
//    public void testAddSuccess() {
//        Board board = new Board(1, "Hello World", null, null);
//        when(boardRepository.save(any(Board.class))).thenReturn(board);
//        ResponseEntity<Board> response = boardController.add(board);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    /**
//     * Tests if the POST request successfully returns BAD REQUEST (error 400) if the board has an empty value as a title
//     */
//    @Test
//    public void testAddBoardWithEmptyTitle() {
//        BoardRepository mockRepo = mock(BoardRepository.class);
//        BoardController controller = new BoardController(new Random(), mockRepo);
//        Board board = new Board("");
//        ResponseEntity<Board> response = controller.add(board);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        verify(mockRepo, never()).save(board);
//    }
//
//    /**
//     * Tests if the POST request successfully returns BAD REQUEST (error 400) if the board has null as a title
//     */
//    @Test
//    public void testPostRequest3() {
//        BoardRepository mockRepo = mock(BoardRepository.class);
//        BoardController controller = new BoardController(new Random(), mockRepo);
//        Board board = new Board(null);
//        ResponseEntity<Board> response = controller.add(board);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        verify(mockRepo, never()).save(board);
//    }
//
//
//    /**
//     * Tests if the PUT request successfully updates (status 200) the board if it exists in the database
//     */
//    @Test
//    public void testPutRequest1() {
//        long boardId = 1L;
//        Board existingBoard = new Board(boardId, "Existing Board", null, null);
//        Board updatedBoard = new Board(boardId, "Updated Board", null, null);
//        when(boardRepository.findById(boardId)).thenReturn(Optional.of(existingBoard));
//        when(boardRepository.save(existingBoard)).thenReturn(updatedBoard);
//
//        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedBoard, response.getBody());
//        verify(boardRepository).findById(boardId);
//        verify(boardRepository).save(existingBoard);
//    }
//
//    /**
//     * Tests if the PUT request successfully returns Not Found (error 404) if the board does not exist in the database
//     */
//    @Test
//    public void testPutRequest2() {
//        long boardId = 1L;
//        Board updatedBoard = new Board(boardId, "Updated Board", null, null);
//        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
//
//        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(boardRepository).findById(boardId);
//        verify(boardRepository, never()).save(any());
//    }
//
//    /**
//     * Tests if the PUT request successfully updates (status 200) the board with the new title and tags if it exists in the database
//     */
//    @Test
//    public void testPutRequest3() {
//        long boardId = 1L;
//        List<Column> columns = new ArrayList<>();
//        List<Tag> tags = Arrays.asList(new Tag(1L, "tag1", null), new Tag(2L, "tag2", null));
//        Board existingBoard = new Board(boardId, "Existing Board", columns, tags);
//        Board updatedBoard = new Board(boardId, "Updated Board", columns, List.of(new Tag(3L, "new tag", null)));
//        when(boardRepository.findById(boardId)).thenReturn(Optional.of(existingBoard));
//        when(boardRepository.save(existingBoard)).thenReturn(existingBoard);
//
//        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedBoard, response.getBody());
//        verify(boardRepository).findById(boardId);
//        verify(boardRepository).save(existingBoard);
//        assertEquals(updatedBoard.getTitle(), existingBoard.getTitle());
//        assertEquals(updatedBoard.getTags(), existingBoard.getTags());
//    }
//
//    /**
//     * Tests if the PUT request successfully updates (status 200) the board with the same title, columns and tags if it already exists in the database
//     */
//    @Test
//    public void testPutRequest4() {
//        long boardId = 1L;
//        List<Column> columns = new ArrayList<>();
//        List<Tag> tags = Arrays.asList(new Tag(1L, "tag1", null), new Tag(2L, "tag2", null));
//        Board existingBoard = new Board(boardId, "Hello World", columns, tags);
//        Board updatedBoard = new Board(boardId, "Hello World", columns, tags);
//        when(boardRepository.findById(boardId)).thenReturn(Optional.of(existingBoard));
//        when(boardRepository.save(existingBoard)).thenReturn(existingBoard);
//
//        ResponseEntity<Board> response = boardController.update(boardId, updatedBoard);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedBoard, response.getBody());
//        verify(boardRepository).findById(boardId);
//        verify(boardRepository).save(existingBoard);
//        assertEquals(updatedBoard.getTitle(), existingBoard.getTitle());
//        assertEquals(updatedBoard.getTags(), existingBoard.getTags());
//    }
//
//    /**
//     * Tests if the DELETE request successfully returns NO CONTENT (error 204) if the board is deleted from the database
//     */
//    @Test
//    public void testDeleteBoardSuccess() {
//        long id = 1;
//        when(boardRepository.existsById(id)).thenReturn(true);
//        ResponseEntity<Void> response = boardController.delete(id);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//    }
//
//    /**
//     * Tests if the DELETE request successfully returns NOT FOUND (error 404) if the board does not exist in the database
//     */
//    @Test
//    public void testDeleteBoardNotFound() {
//        long id = 1;
//        when(boardRepository.existsById(id)).thenReturn(false);
//        ResponseEntity<Void> response = boardController.delete(id);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}
