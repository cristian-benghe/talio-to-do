/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Card;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MainCtrlTest {

    private MainCtrl mainCtrl;

    private Stage primaryStage;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private MainOverviewCtrl mainOverviewCtrl;
    private Scene mainOverview;


    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private Scene popupStage;

    private CardViewCtrl cardViewCtrl;
    private Scene cardView;
    private BoardCustomizationCtrl boardCustomizationCtrl;

    private boolean isAdmin = false;

    private Scene boardCustomization;
    private TagViewCtrl tagViewCtrl;
    private Scene tagView;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        mainCtrl = new MainCtrl();

        primaryStage = mock(Stage.class);
        boardOverviewCtrl = mock(BoardOverviewCtrl.class);
        boardOverview = mock(Scene.class);
        mainOverviewCtrl = mock(MainOverviewCtrl.class);
        mainOverview = mock(Scene.class);
        clientConnectCtrl = mock(ClientConnectCtrl.class);
        clientConnect = mock(Scene.class);
        popupStage = mock(Scene.class);
        cardViewCtrl = mock(CardViewCtrl.class);
        cardView = mock(Scene.class);
        boardCustomizationCtrl = mock(BoardCustomizationCtrl.class);
        isAdmin = false;
        boardCustomization = mock(Scene.class);
        tagViewCtrl = mock(TagViewCtrl.class);
        tagView = mock(Scene.class);
        primaryStage = mock(Stage.class);


        mainCtrl.setBoardOverviewCtrl(boardOverviewCtrl);
        mainCtrl.setMainOverviewCtrl(mainOverviewCtrl);
        mainCtrl.setCardViewCtrl(cardViewCtrl);
        mainCtrl.setClientConnectCtrl(clientConnectCtrl);
        mainCtrl.setTagViewCtrl(tagViewCtrl);
        mainCtrl.setPrimaryStage(primaryStage);
    }




    @Test
    public void testCreateConnection() {
        mainCtrl.createConnection("");
        Mockito.verify(mainCtrl.getClientConnectCtrl(), times(1)).setConnection("");
        Mockito.verify(mainCtrl.getMainOverviewCtrl(), times(1)).setConnection("");
        Mockito.verify(mainCtrl.getBoardOverviewCtrl(), times(1)).setConnection("");
        Mockito.verify(mainCtrl.getCardViewCtrl(), times(1)).setConnection("");
        Mockito.verify(mainCtrl.getTagViewCtrl(), times(1)).setConnection("");

    }

    @Test
    public void testShowDeleteBoardPopUp() {

//        Stage mockStage = mock(Stage.class);
//        doNothing().when(mockStage).setScene(any(Scene.class));
//
//        mainCtrl.setPopupStage(mockStage.getScene());
//        //doNothing().when(mainCtrl.getPrimaryStage());
//        //Mockito.verify(mainCtrl.getPrimaryStage(), times(1)).setTitle("Delete_Pop_Up");
//
//        mainCtrl.showDeleteBoardPopUp("", -1L);


    }

    @Test
    public void testGetBoardId() {
        when(boardOverviewCtrl.getId()).thenReturn(1L);
        MainCtrl ctrl = new MainCtrl();
        ctrl.setBoardOverviewCtrl(boardOverviewCtrl);
        Long boardId = ctrl.getBoardId();
        assertEquals(Optional.of(1L), Optional.of(boardId));
    }


    @Test
    public void testGetTagViewCtrl() {
        MainCtrl ctrl = new MainCtrl();
        TagViewCtrl tagViewCtrl = ctrl.getTagViewCtrl();
        assertNull(tagViewCtrl);
    }

    @Test
    public void testSetAndGetCard() {
        MainCtrl ctrl = new MainCtrl();
        Card card1 = new Card();
        Card card2 = new Card();
        ctrl.setCard(card1);
        assertEquals(card1, ctrl.getCard());
        ctrl.setCard(card2);
        assertEquals(card2, ctrl.getCard());
    }

    @Test
    public void testGetCardViewCtrl() {
        MainCtrl ctrl = new MainCtrl();
        CardViewCtrl cardViewCtrl = ctrl.getCardViewCtrl();
        assertNull(cardViewCtrl);
    }

    @Test
    public void testGetBoardOverviewCtrl() {
        MainCtrl ctrl = new MainCtrl();
        BoardOverviewCtrl boardOverviewCtrl = ctrl.getBoardOverviewCtrl();
        assertNull(boardOverviewCtrl);
    }

    @Test
    public void testGetMainOverviewCtrl() {
        MainCtrl ctrl = new MainCtrl();
        MainOverviewCtrl mainOverviewCtrl = ctrl.getMainOverviewCtrl();
        assertNull(mainOverviewCtrl);
    }

    @Test
    public void testGetClientConnectCtrl() {
        MainCtrl ctrl = new MainCtrl();
        ClientConnectCtrl clientConnectCtrl = ctrl.getClientConnectCtrl();
        assertNull(clientConnectCtrl);
    }

    @Test
    public void testGetCardView() {
        MainCtrl ctrl = new MainCtrl();
        Scene cardView = ctrl.getCardView();
        assertNull(cardView);
    }

    @Test
    public void testIsAdmin() {
        MainCtrl ctrl = new MainCtrl();
        boolean isAdmin = ctrl.isAdmin();
        assertFalse(isAdmin);
    }

    @Test
    public void testGetTagView() {
        MainCtrl ctrl = new MainCtrl();
        Scene tagView = ctrl.getTagView();
        assertNull(tagView);
    }

    @Test
    public void testIsShownMainOverviewOneTime() {
        MainCtrl ctrl = new MainCtrl();
        boolean shownMainOverviewOneTime = ctrl.isShownMainOverviewOneTime();
        assertFalse(shownMainOverviewOneTime);
    }
    @Test
    public void testGetClientConnectWithNull() {
        MainCtrl instance = new MainCtrl();
        Scene result = instance.getClientConnect();
        assertNull(result);
    }
    @Test
    public void testGetMainOverviewWithNull() {
        MainCtrl instance = new MainCtrl();
        Scene result = instance.getMainOverview();
        assertNull(result);
    }
    @Test
    public void testGetBoardOverviewWithNull() {
        MainCtrl instance = new MainCtrl();
        Scene result = instance.getBoardOverview();
        assertNull(result);
    }
    @Test
    public void testGetCardViewCtrlWithNull() {
        MainCtrl instance = new MainCtrl();
        CardViewCtrl result = instance.getcardViewCtrl();
        assertNull(result);
    }
    @Test
    public void testGetAdminPasswordWithNull() {
        MainCtrl instance = new MainCtrl();
        String result = instance.getAdminPassword();
        assertNull(result);
    }
    @Test
    public void testSetAdminPasswordWithNull() {
        MainCtrl instance = new MainCtrl();
        instance.setAdminPassword(null);
        String result = instance.getAdminPassword();
        assertNull(result);
    }
    @Test
    public void testIsHasAdminRoleWithNull() {
        MainCtrl instance = new MainCtrl();
        boolean result = instance.isHasAdminRole();
        assertFalse(result);
    }
    @Test
    public void testSetHasAdminRoleWithNull() {
        MainCtrl instance = new MainCtrl();
        instance.setHasAdminRole(false);
        boolean result = instance.isHasAdminRole();
        assertFalse(result);
    }
    @Test
    public void testGetPrimaryStageWithNull() {
        MainCtrl instance = new MainCtrl();
        Stage result = instance.getPrimaryStage();
        assertNull(result);
    }
    @Test
    public void testGetClientConnect() {
        mainCtrl.setClientConnectCtrl(null);
        assertEquals(null, mainCtrl.getClientConnect());
    }
    @Test
    public void testGetPrimaryStage() {
        mainCtrl.setPrimaryStage(null);
        assertEquals(null, mainCtrl.getPrimaryStage());
    }




}