package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.catalina.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardOverviewCtrlTest {


    private MainCtrl mainCtrl;
    private ServerUtils server;
    private BoardOverviewCtrl boardOverviewCtrl;

    @BeforeEach
    public void setup() {


        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);

        boardOverviewCtrl = new BoardOverviewCtrl(server, mainCtrl);


    }

    @Test
    void socketsCall() {
        ServerUtils server = mock(ServerUtils.class);
        Board board = new Board();
        board.setId(1L);

        boardOverviewCtrl.setId(1L);

        boardOverviewCtrl.socketsCall();

        verify(server, times(3));
    }

    @Test
    void toRgbCode() {

        // Test with black color
        String expected = "#000000";
        String actual = boardOverviewCtrl.toRgbCode(Color.BLACK);
        assertEquals(expected, actual);

// Test with white color
        expected = "#FFFFFF";
        actual = boardOverviewCtrl.toRgbCode(Color.WHITE);
        assertEquals(expected, actual);

// Test with red color
        expected = "#FF0000";
        actual = boardOverviewCtrl.toRgbCode(Color.RED);
        assertEquals(expected, actual);

// Test with green color
        expected = "#008000";
        actual = boardOverviewCtrl.toRgbCode(Color.GREEN);
        assertEquals(expected, actual);

// Test with blue color
        expected = "#0000FF";
        actual = boardOverviewCtrl.toRgbCode(Color.BLUE);
        assertEquals(expected, actual);

    }
    

}