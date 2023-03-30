package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;

public class MainCtrl {

    private Stage primaryStage;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private MainOverviewCtrl mainOverviewCtrl;
    private Scene mainOverview;


    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private DeleteBoardPopUpCtrl deleteBoardPopUpCtrl;
    private Scene popupStage;

    private CardViewCtrl cardViewCtrl;
    private Scene cardView;
    private BoardCustomizationCtrl boardCustomizationCtrl;

    private boolean isAdmin=false;

    private Scene boardCustomization;
    private TagViewCtrl tagViewCtrl;
    private Scene tagView;

    //private TagTemplateCtrl tagTemplateCtrl;
    //private Scene tagTemplate;
    private boolean shownMainOverviewOneTime = false;
    private String adminPassword;
    private boolean hasAdminRole;

    /**
     * This method initializes this controller instances
     * @param primaryStage an injection of the primary stage
     * @param mainOverview an injection of the MainOverview scene and controller
     * @param boardOverview an injection of the BoardOverview scene and controller
     * @param clientConnect an injection of the ClientConnect scene and controller
     * @param popupStage an injection of the PopupStage scene and controller
     * @param cardView an injection of the CardView scene and controller
     * @param boardCustomization on injection of the boardCustomization scene and controller
     * @param tagView an injection of the CardView scene and controller
     * @throws Exception an exception that may be thrown
     */

    public void initialize(Stage primaryStage,
                           Pair<MainOverviewCtrl, Parent> mainOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<DeleteBoardPopUpCtrl, Parent> popupStage,
                           Pair<CardViewCtrl, Parent> cardView,
                           Pair<BoardCustomizationCtrl, Parent> boardCustomization,
                           Pair<TagViewCtrl, Parent> tagView) throws Exception {


        this.isAdmin = false;
        this.primaryStage = primaryStage;

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.mainOverviewCtrl = mainOverview.getKey();
        this.mainOverview = new Scene(mainOverview.getValue());

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.cardViewCtrl = cardView.getKey();
        this.cardView = new Scene(cardView.getValue());
        this.boardCustomizationCtrl=boardCustomization.getKey();
        this.boardCustomization=new Scene(boardCustomization.getValue());
        this.tagViewCtrl = tagView.getKey();
        this.tagView = new Scene(tagView.getValue());


        //Set the primary stage to be not resizable
        primaryStage.setResizable(false);

        //Set the stage icon
        //TODO Replace the temporary icon
        primaryStage.getIcons().add(new Image("RandomIcon.png"));


        showClientConnect();
        primaryStage.show();
    }


    /**
     * @param text the title of the board, to get the id
     * @param blue from rgb
     * @param green from rgb
     * @param red from rgb
     */
    public void showBoardOverview(String text, Double blue, Double green, Double red) {
        boardCustomizationCtrl.setBoardText(text);
        System.out.println(text);

        boardOverviewCtrl.socketsCall();

        primaryStage.setTitle("Talio - Board View");
        primaryStage.setScene(boardOverview);
        primaryStage.centerOnScreen();
        boardOverviewCtrl.setBoardTitle(text, blue, green, red);
    }

    /**
     * This method changes the scene to the MainOverview scene.
     */
    public void showMainOverview() {

        if(!shownMainOverviewOneTime)
            mainOverviewCtrl.socketsCall();
        primaryStage.setTitle("Talio - Home");
        primaryStage.setScene(mainOverview);
        primaryStage.centerOnScreen();
        //Refresh the Scene
        mainOverviewCtrl.refreshOverview();
        shownMainOverviewOneTime = true;

    }

    /**
     * This method changes the scene to the ClientConnectScene.
     */
    public void showClientConnect() {
        primaryStage.setTitle("Talio - Connect to a Server");

        primaryStage.setScene(clientConnect);
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();

    }

    /**
     * A method to switch the scene from boardOverView to the CarView
     */
    public void showCardView() {
        cardViewCtrl.setText(boardOverviewCtrl.getTitle());
        primaryStage.setTitle("Talio - CardView");
        primaryStage.setScene(cardView);
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();
    }
    /**
     * A method to switch the scene to the TagView
     */
    public void showTagView() throws IOException {
        primaryStage.setTitle("Talio - TagView");
        primaryStage.setScene(tagView);
        tagViewCtrl.refreshtaglist();
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();

    }

    /**
     * A method to switch the scene to the TagView
     */


    /**
     * Displays a popup window to confirm the deletion of a board with the given title and ID.
     *
     * @param title - the title of the board to be deleted.
     * @param id - the ID of the board to be deleted.
     */
    public void showDeleteBoardPopUp(String title, Long id){
        primaryStage.setTitle("Delete_Pop_Up");
        primaryStage.setScene(popupStage);
        primaryStage.centerOnScreen();
        deleteBoardPopUpCtrl.setText(title);
        deleteBoardPopUpCtrl.setID(id);
    }

    /**
     * A new method to connect to a given address.
     * @param address the address of the server
     */
    public void createConnection(String address){
        clientConnectCtrl.setConnection(address);
        mainOverviewCtrl.setConnection(address);
        boardOverviewCtrl.setConnection(address);
        boardCustomizationCtrl.setConnection(address);
        cardViewCtrl.setConnection(address);
        tagViewCtrl.setConnection(address);
    }

    /**
     * gets the value of the AdminPassword
     *
     * @return the value of the AdminPassword
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Sets the value of AdminPassword
     *
     * @param adminPassword - the value of the AdminPassword
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * gets the value of the hasAdminRole
     *
     * @return the value of the hasAdminRole
     */
    public boolean isHasAdminRole() {
        return hasAdminRole;
    }

    /**
     * Sets the value of hasAdminRole
     *
     * @param hasAdminRole - the value of the hasAdminRole
     */
    public void setHasAdminRole(boolean hasAdminRole) {
        this.hasAdminRole = hasAdminRole;
    }

    /**
     * changes the scene to board customization
     */
    public void showBoardCustomization() {
        primaryStage.setTitle("Board Customization");
        primaryStage.setScene(boardCustomization);
    }

    /**
     * @return the id of the board
     */
    public Long getBoardId() {
        return boardOverviewCtrl.getId();
    }
}
