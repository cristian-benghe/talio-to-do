package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertTrue;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import client.scenes.BoardOverviewCtrl;
import javafx.scene.input.DataFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BoardOverviewCtrlTest {
    private BoardOverviewCtrl boardOverviewCtrl;
    private Clipboard clipboard;

    @BeforeEach
    void setUp() {
        boardOverviewCtrl = new BoardOverviewCtrl(mock(ServerUtils.class), mock(MainCtrl.class));
    }

    @Test
    void testCopyID1() {
        boardOverviewCtrl.setBoardTitle("New Column -- 12"); // this line causes an exception
        boardOverviewCtrl.copyID();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        assertTrue(clipboard.hasString());
        assertEquals("12", clipboard.getString());
    }

    @Test
    void testCopyID2() {
        boardOverviewCtrl.setBoardTitle("Amazing -- 12"); // this line causes an exception
        boardOverviewCtrl.copyID();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        assertTrue(clipboard.hasString());
        assertEquals("12", clipboard.getString());
    }

}
