package client.scenes;

import commons.Card;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class MainCtrl {

    private Stage primaryStage;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private MainOverviewCtrl mainOverviewCtrl;
    private Scene mainOverview;

    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private CardViewCtrl cardViewCtrl;
    private Scene cardView;

    private ColorManagementCtrl colorManagementCtrl;
    private Scene colorManagement;

    private boolean isAdmin=false;

    private TagViewCtrl tagViewCtrl;
    private Scene tagView;

    //private TagTemplateCtrl tagTemplateCtrl;
    //private Scene tagTemplate;
    private boolean shownMainOverviewOneTime = false;
    private String adminPassword;
    private boolean hasAdminRole;
    private Card card;


    /**
     * This method initializes this controller instances
     * @param primaryStage an injection of the primary stage
     * @param mainOverview an injection of the MainOverview scene and controller
     * @param boardOverview an injection of the BoardOverview scene and controller
     * @param clientConnect an injection of the ClientConnect scene and controller
     * @param cardView an injection of the CardView scene and controller
     * @param tagView an injection of the CardView scene and controller
     * @param colorManagement an injection of the colormanagement scene and controller
     * @throws Exception an exception that may be thrown
     */

    public void initialize(Stage primaryStage,
                           Pair<MainOverviewCtrl, Parent> mainOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<CardViewCtrl, Parent> cardView,
                           Pair<TagViewCtrl, Parent> tagView,
                           Pair<ColorManagementCtrl, Parent>colorManagement) throws Exception {


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

        this.tagViewCtrl = tagView.getKey();
        this.tagView = new Scene(tagView.getValue());

        this.colorManagementCtrl = colorManagement.getKey();
        this.colorManagement = new Scene(colorManagement.getValue());


        //Set the primary stage to be not resizable
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> {
            cardViewCtrl.stopLongPolling();
        });


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
        boardOverviewCtrl.socketsCall();
        String css = getClass().getResource("/board.css").toExternalForm();
        //add stylsheet
        boardOverview.getStylesheets().add(css);
        primaryStage.setTitle("Talio - Board View");
        //Set the long-polling
        boardOverviewCtrl.resetLongPolling();
        boardOverviewCtrl.setUpLongPolling();
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.setBoardTitle(text, blue, green, red);
    }

    /**
     * This method changes the scene to the MainOverview scene.
     */
    public void showMainOverview() {
        //add stylsheet
        String css = getClass().getResource("/home.css").toExternalForm();
        mainOverview.getStylesheets().add(css);

        if(!shownMainOverviewOneTime)
            mainOverviewCtrl.socketsCall();
        mainOverviewCtrl.loadUserWorkspace();

        primaryStage.setTitle("Talio - Home");
        primaryStage.setScene(mainOverview);
        //Refresh the Scene
        mainOverviewCtrl.refreshOverview();
        shownMainOverviewOneTime = true;

    }

    /**
     * This method changes the scene to the ClientConnectScene.
     */
    public void showClientConnect() {
        String css = getClass().getResource("/client.css").toExternalForm();
        clientConnect.getStylesheets().add(css);
        primaryStage.setTitle("Talio - Connect to a Server");
        boardOverviewCtrl.resetLongPolling();

        primaryStage.setScene(clientConnect);
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();

    }

    /**
     * A method to switch the scene from boardOverView to the CarView
     * @param card the card instance that will be inspected
     */
    public void showCardView(Card card) {
        String css = getClass().getResource("/card.css").toExternalForm();
        cardView.getStylesheets().add(css);
        cardViewCtrl.setText(boardOverviewCtrl.getTitle());
        primaryStage.setTitle("Talio - CardView");
        cardViewCtrl.setCard(card);
        this.card = card;
        primaryStage.setScene(cardView);
        cardViewCtrl.refresh();
        cardViewCtrl.resetLongPolling();
        cardViewCtrl.setUpLongPolling();
    }
    /**
     * A method to switch the scene to the TagView
     */
    public void showTagView() throws IOException {
        String css = getClass().getResource("/tagview.css").toExternalForm();
        cardViewCtrl.resetLongPolling();
        tagView.getStylesheets().add(css);
        tagViewCtrl.setCard(card);
        primaryStage.setTitle("Talio - TagView");
        primaryStage.setScene(tagView);
        tagViewCtrl.refreshtaglist();
        clientConnectCtrl.refresh();
    }

    /**
     * A method that shows the tagView from shortcut 'C'
     * @param card a card to be showed
     * @throws IOException exception to be thrown
     */
    public void showTagViewShortcut(Card card) throws IOException {
        String css = getClass().getResource("/tagview.css").toExternalForm();
        tagView.getStylesheets().add(css);
        tagViewCtrl.setCard(card);
        primaryStage.setTitle("Talio - TagView");
        primaryStage.setScene(tagView);
        System.out.println(card.getTags().size()+"fg ");
        tagViewCtrl.refreshtaglist();
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();

    }
    /**
     * A new method to connect to a given address.
     * @param address the address of the server
     */
    public void createConnection(String address){
        //sets the connection of all controllers with the server
        clientConnectCtrl.setConnection(address);
        mainOverviewCtrl.setConnection(address);
        boardOverviewCtrl.setConnection(address);
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
     * @return the id of the board
     */
    public Long getBoardId() {
        return boardOverviewCtrl.getId();
    }

    /**
     * @return the tagviewctrl to access the refresh method in tagview when deleting a templatetag
     */
    public TagViewCtrl getTagViewCtrl() {
        return tagViewCtrl;
    }

    /**
     * @param card the current card in the ctrl
     */
    public void setCard(Card card) {
        this.card=card;
    }

    /**
     * @return the last card entered
     */
    public Card getCard() {
        return card;
    }

    /**
     * @return the cardviewctrl
     */
    public CardViewCtrl getcardViewCtrl() {
        return cardViewCtrl;
    }

    /**

     Returns the controller for the board overview.
     @return the board overview controller
     */
    public BoardOverviewCtrl getBoardOverviewCtrl() {
        return boardOverviewCtrl;
    }
    /**

     Returns the scene for the board overview.
     @return the board overview scene
     */
    public Scene getBoardOverview() {
        return boardOverview;
    }
    /**

     Returns the controller for the main overview.
     @return the main overview controller
     */
    public MainOverviewCtrl getMainOverviewCtrl() {
        return mainOverviewCtrl;
    }
    /**

     Returns the scene for the main overview.
     @return the main overview scene
     */
    public Scene getMainOverview() {
        return mainOverview;
    }
    /**

     Returns the controller for the client connect.
     @return the client connect controller
     */
    public ClientConnectCtrl getClientConnectCtrl() {
        return clientConnectCtrl;
    }
    /**

     Returns the scene for the client connect.
     @return the client connect scene
     */
    public Scene getClientConnect() {
        return clientConnect;
    }

    /**

     Returns the controller for the card view.
     @return the card view controller
     */
    public CardViewCtrl getCardViewCtrl() {
        return cardViewCtrl;
    }
    /**

     Returns the scene for the card view.
     @return the card view scene
     */
    public Scene getCardView() {
        return cardView;
    }
    /**

     Returns whether the user is an admin or not.
     @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isAdmin;
    }
    /**

     Returns the scene for the tag view.
     @return the tag view scene
     */
    public Scene getTagView() {
        return tagView;
    }
    /**

     Returns whether the main overview has been shown once or not.
     @return true if the main overview has been shown once, false otherwise
     */
    public boolean isShownMainOverviewOneTime() {
        return shownMainOverviewOneTime;
    }
    /**

     Sets the primary stage of the application.
     @param primaryStage the new primary stage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * setter for boardOverview ctrl
     * @param boardOverviewCtrl the ctrl to be set
     */
    public void setBoardOverviewCtrl(BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    /**

     Sets the MainOverviewCtrl.
     @param mainOverviewCtrl The new MainOverviewCtrl to set.
     */
    public void setMainOverviewCtrl(MainOverviewCtrl mainOverviewCtrl) {
        this.mainOverviewCtrl = mainOverviewCtrl;
    }
    /**

     Sets the ClientConnectCtrl.
     @param clientConnectCtrl The new ClientConnectCtrl to set.
     */
    public void setClientConnectCtrl(ClientConnectCtrl clientConnectCtrl) {
        this.clientConnectCtrl = clientConnectCtrl;
    }

    /**

     Sets the CardViewCtrl.
     @param cardViewCtrl The new CardViewCtrl to set.
     */
    public void setCardViewCtrl(CardViewCtrl cardViewCtrl) {
        this.cardViewCtrl = cardViewCtrl;
    }
    /**

     Sets the TagViewCtrl.
     @param tagViewCtrl The new TagViewCtrl to set.
     */
    public void setTagViewCtrl(TagViewCtrl tagViewCtrl) {
        this.tagViewCtrl = tagViewCtrl;
    }

    /**
     * getter for the primary stage
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @return boardcontroller
     */
    public BoardOverviewCtrl getBoardCtrl() {
        return boardOverviewCtrl;
    }

    /**
     * show color management
     */
    public void showColorManagment(){
        //colorManagementCtrl.setColorPick();
        colorManagementCtrl.setColorPickColumn();
        String css = getClass().getResource("/colorMcss.css").toExternalForm();
        colorManagement.getStylesheets().add(css);
        primaryStage.setTitle("Color management");
        primaryStage.setScene(colorManagement);
    }

    /**
     * @return colormanagementctrl
     */
    public ColorManagementCtrl getColorManagement() {
        return colorManagementCtrl;
    }

    /**
     * default color set
     */
    public void setColorManagementDefault() {
        colorManagementCtrl.setColorPick();
    }
}
