package client;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardDataTest {

    BoardData boardData;

    @BeforeEach
    void setUp()  {
        boardData = new BoardData();
    }

//    @Test
//    void serializeBoardData() throws IOException, ClassNotFoundException {
//        File file = new File("test.txt");
//        file.createNewFile();
//        Map<String, List<Board>> test = new HashMap<>();
//        Board board = new Board();
//        test.put("1.0.0.0", List.of(board));
//        boardData.serializeBoardData("test.txt");
//        boardData.deserializeBoardData("test.txt");
//        assertEquals(test, boardData.getBoardMap());
//    }

    @Test
    void deserializeBoardData() {
    }

    @Test
    void getBoardMap() {
    }

    @Test
    void setBoardMap() {
    }

    @Test
    void getBoardList() {
    }

    @Test
    void setBoardList() {
    }
}