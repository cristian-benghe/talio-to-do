package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertTrue;

class CardViewCtrlTest {

    MainCtrl mainCtrl;
    ServerUtils server;
    CardViewCtrl cardViewCtrl;
    CardViewCtrlTester mockCardViewCtrl;
    @Mock
    private Card card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);
        card = new Card("card", "desc", null, null);
        card.setId((long) -1);
        cardViewCtrl = new CardViewCtrl(server, mainCtrl);
        mockCardViewCtrl = new CardViewCtrlTester(server, mainCtrl);
    }

    @Test
    void toRgbCode() {

        // Test with black color
        String expected = "#000000";
        String actual = cardViewCtrl.toRgbCode(javafx.scene.paint.Color.BLACK);
        assertEquals(expected, actual);

// Test with white color
        expected = "#FFFFFF";
        actual = cardViewCtrl.toRgbCode(javafx.scene.paint.Color.WHITE);
        assertEquals(expected, actual);

// Test with red color
        expected = "#FF0000";
        actual = cardViewCtrl.toRgbCode(javafx.scene.paint.Color.RED);
        assertEquals(expected, actual);

// Test with green color
        expected = "#008000";
        actual = cardViewCtrl.toRgbCode(javafx.scene.paint.Color.GREEN);
        assertEquals(expected, actual);

// Test with blue color
        expected = "#0000FF";
        actual = cardViewCtrl.toRgbCode(Color.BLUE);
        assertEquals(expected, actual);

    }

    @Test
    void getTagViewTest() throws IOException {
        cardViewCtrl.getTagView();
        verify(mainCtrl, times(1)).showTagView();
    }

    @Test
    public void testSetCard() {
        when(server.getAllTasksByCardId(card.getId())).thenReturn(null);
        cardViewCtrl.setCard(card);
        verify(server).getAllTasksByCardId(card.getId());
    }


    @Test
    public void testGetBackCard() {
        cardViewCtrl.setText("test");
        cardViewCtrl.getBackCard();
        verify(mainCtrl).showBoardOverview("test", 1.0, 1.0, 1.0);
    }

//    @Test
//    public void testRefresh() {
//        cardViewCtrl.setCard(card);
//        when(card.getTaskList()).thenReturn(null);
//        cardViewCtrl.setLongDescription(new TextArea());
//        cardViewCtrl.refresh();
//        verify(card).getTaskList();
//    }

    @Test
    public void testGetCard() {
        cardViewCtrl.setText("test");
        cardViewCtrl.getBackCard();
        verify(mainCtrl).showBoardOverview("test", 1.0, 1.0, 1.0);
    }

    @Test
    public void testGetTagView() throws Exception {

        cardViewCtrl.getTagView();
        verify(mainCtrl, times(1)).showTagView();
    }

    @Test
    public void testGetCardBack() {
        cardViewCtrl.setText("test_text");

        cardViewCtrl.getBackCard();

        verify(mainCtrl).showBoardOverview(eq("test_text"), eq(1.0), eq(1.0), eq(1.0));
    }

    @Test
    public void test2SetCard() {
        Card mockCard = mock(Card.class);
        when(mockCard.getId()).thenReturn(1L);
        when(server.getAllTasksByCardId(1L)).thenReturn(new ArrayList<>());

        cardViewCtrl.setCard(mockCard);

        verify(mockCard).setTaskList(any());
        verify(server).getAllTasksByCardId(1L);
        assertEquals(mockCard, cardViewCtrl.getCard());
    }

    @Test
    public void testSetText() {
        cardViewCtrl.setText("Test text");
        assertEquals("Test text", cardViewCtrl.getText());
    }

    @Test
    void testGetLongDescription() {
        TextArea actualTextArea = cardViewCtrl.getLongDescription();
        assertNull(actualTextArea);
    }

    @Test
    void testGetTitleLabel() {
        cardViewCtrl.setTitleLabel(null);
        assertNull(cardViewCtrl.getTitleLabel());
    }

    @Test
    void testGetTaskList() {
        cardViewCtrl.setTaskList(null);
        assertNull(cardViewCtrl.getTaskList());
    }

    @Test
    void testGetEmptyTaskList() {
        cardViewCtrl.setEmptyTaskList(null);
        assertNull(cardViewCtrl.getEmptyTaskList());
    }

    @Test
    void testSetLongDescription() {
        TextArea testLongDescription = null;
        cardViewCtrl.setLongDescription(testLongDescription);
        assertEquals(testLongDescription, cardViewCtrl.getLongDescription());
    }

    @Test
    void testIsTaskDragged() {
        cardViewCtrl.setTaskDragged(true);
        assertTrue(cardViewCtrl.isTaskDragged());
    }

    @Test
    void testIsDraggedOverBin() {
        cardViewCtrl.setDraggedOverBin(true);
        assertTrue(cardViewCtrl.isDraggedOverBin());
    }

    @Test
    void testGetBinContraction() {
        ScaleTransition testBinContraction = new ScaleTransition(Duration.seconds(1));
        cardViewCtrl.setBinContraction(testBinContraction);
        assertEquals(testBinContraction, cardViewCtrl.getBinContraction());
    }

    @Test
    void testGetBinExpansion() {
        ScaleTransition testBinExpansion = new ScaleTransition(Duration.seconds(1));
        cardViewCtrl.setBinExpansion(testBinExpansion);
        assertEquals(testBinExpansion, cardViewCtrl.getBinExpansion());
    }

    @Test
    void testGetBinImage() {
        ImageView testBinImage = null;
        cardViewCtrl.setBinImage(null);
        assertEquals(null, cardViewCtrl.getBinImage());
    }

    @Test
    void testSetTaskList() {
        VBox testTaskList = new VBox();
        cardViewCtrl.setTaskList(testTaskList);
        assertEquals(testTaskList, cardViewCtrl.getTaskList());
    }

    @Test
    void testSetEmptyTaskList() {
        Label testEmptyTaskList = null;
        cardViewCtrl.setEmptyTaskList(testEmptyTaskList);
        assertEquals(testEmptyTaskList, cardViewCtrl.getEmptyTaskList());
    }

    @Test
    void testSetTaglist() {
        HBox testTaglist = new HBox();
        cardViewCtrl.setTaglist(testTaglist);
        assertEquals(testTaglist, cardViewCtrl.getTaglist());
    }

}