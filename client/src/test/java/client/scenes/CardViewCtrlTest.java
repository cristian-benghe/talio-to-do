package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CardViewCtrlTest {

    MainCtrl mainCtrl;
    ServerUtils server;
    CardViewCtrl cardViewCtrl;
    CardViewCtrlTester mockCardViewCtrl;
    @Mock
    private Card card;

    @BeforeEach
    void setUp() {
        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);
        card = new Card("card", "desc", null, null);
        card.setId((long) -1);
        cardViewCtrl = new CardViewCtrl(server, mainCtrl);
        mockCardViewCtrl = new CardViewCtrlTester(server, mainCtrl);
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

}