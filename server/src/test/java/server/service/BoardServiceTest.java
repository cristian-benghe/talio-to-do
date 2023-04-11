package server.service;

import commons.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    private BoardService boardService;
    private BoardRepository boardRepository;

    @BeforeEach
    public void setUp() {
        boardRepository = Mockito.mock(BoardRepository.class);
        boardService = new BoardService(boardRepository);
    }

    @Test
    public void testGetAll() {
        List<Board> boards = new ArrayList<>();
        boards.add(new Board("Test Board 1"));
        boards.add(new Board("Test Board 2"));
        when(boardRepository.findAll()).thenReturn(boards);

        List<Board> result = boardService.getAll();

        assertEquals(2, result.size());
        assertEquals("Test Board 1", result.get(0).getTitle());
        assertEquals("Test Board 2", result.get(1).getTitle());
    }

    @Test
    public void testGetById() {
        Board board = new Board("Test Board");
        board.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        Optional<Board> result = boardService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Board", result.get().getTitle());
    }

    @Test
    public void testGetByIdNotFound() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Board> result = boardService.getById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAdd() {
        Board boardToAdd = new Board("Test Board");
        Board boardAdded = new Board("Test Board");
        boardAdded.setId(1L);
        when(boardRepository.save(boardToAdd)).thenReturn(boardAdded);

        Board result = boardService.add(boardToAdd);

        assertEquals(1L, result.getId());
        assertEquals("Test Board", result.getTitle());
    }

    @Test
    public void testAddNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.add(new Board(null));
        });
    }

    @Test
    public void testAddEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.add(new Board(""));
        });
    }

    @Test
    public void testUpdate() {
        Board existingBoard = new Board("Test Board");
        existingBoard.setId(1L);
        Board updatedBoard = new Board("Updated Test Board");
        updatedBoard.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(existingBoard));
        when(boardRepository.save(updatedBoard)).thenReturn(updatedBoard);

        Board result = boardService.update(1L, updatedBoard);

        assertEquals("Updated Test Board", result.getTitle());
    }

    @Test
    public void testUpdateNotFound() {
        Board updatedBoard = new Board("Updated Test Board");
        updatedBoard.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            boardService.update(1L, updatedBoard);
        });
    }

    @Test
    public void delete_WhenBoardExists_ShouldDelete() {
        when(boardRepository.existsById(15L)).thenReturn(true);

        boardService.delete(15L);

        verify(boardRepository).deleteById(15L);
    }

    @Test
    public void delete_WhenBoardDoesNotExist_ShouldThrowException() {
        // Arrange
        long boardId = 416L;
        when(boardRepository.existsById(boardId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.delete(boardId);
        });
    }
}