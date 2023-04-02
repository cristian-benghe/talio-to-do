package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardViewCtrlTest {

    MainCtrl mainCtrl;
    ServerUtils server;
    CardViewCtrl cardViewCtrl;

    @BeforeEach
    void setUp() {
        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);
        cardViewCtrl = new CardViewCtrl(server, mainCtrl);
    }

    @Test
    void getTagViewTest() throws IOException {
        cardViewCtrl.getTagView();
        verify(mainCtrl,times(1)).showTagView();
    }
}