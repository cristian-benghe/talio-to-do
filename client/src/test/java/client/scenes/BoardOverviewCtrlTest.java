package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.catalina.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class BoardOverviewCtrlTest {

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils server;
    private BoardOverviewCtrl boardOverviewCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        boardOverviewCtrl = new BoardOverviewCtrl(server, mainCtrl);
    }

    @Test
    void setConnection() {


        // call the showHelp method
        boardOverviewCtrl.setConnection("");

        // verify that the mainCtrl's showMainOverview method was called
        verify(server, times(1)).setServerAddress("");
    }



    @Test
    void socketsCall() {

        boardOverviewCtrl.socketsCall();

        verify(server, times(3)).registerForMessages(any(),any(),any());
    }

    @Test
    void toRgbCode() {
        //We will do some tests for some different colors chosen randomly

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

    @Test
    public void tests() {
        boardOverviewCtrl.setNrCol(null);
        boardOverviewCtrl.setDeleteBoardDialog(null);
        boardOverviewCtrl.setHelpDialog(null);
        boardOverviewCtrl.setCardCustomization(null);
        boardOverviewCtrl.setAnchorPane(null);
        boardOverviewCtrl.setBoardTitleLabel(null);
        boardOverviewCtrl.setBoardTitle(null);
        boardOverviewCtrl.setHbox(null);
        boardOverviewCtrl.setKeyID(null);
        boardOverviewCtrl.setBinImage(null);
        boardOverviewCtrl.setCopyLabel(null);
        boardOverviewCtrl.setSelectedAnchorPane(null);
        boardOverviewCtrl.setBinContraction(null);
        boardOverviewCtrl.setBinExpansion(null);
        boardOverviewCtrl.setTitle(null);

        assert boardOverviewCtrl.getNrCol() == null;
        assert boardOverviewCtrl.getDeleteBoardDialog() == null;
        assert boardOverviewCtrl.getHelpDialog() == null;
        assert boardOverviewCtrl.getCardCustomization() == null;
        assert boardOverviewCtrl.getAnchorPane() == null;
        assert boardOverviewCtrl.getBoardTitleLabel() == null;
        assert boardOverviewCtrl.getBoardTitle() == null;
        assert boardOverviewCtrl.getHbox() == null;
        assert boardOverviewCtrl.getKeyID() == null;
        assert boardOverviewCtrl.getBinImage() == null;
        assert boardOverviewCtrl.getCopyLabel() == null;
        assert boardOverviewCtrl.getSelectedAnchorPane() == null;
        assert boardOverviewCtrl.getBinContraction() == null;
        assert boardOverviewCtrl.getBinExpansion() == null;
        assertNull(boardOverviewCtrl.getTitle());
    }
    

}