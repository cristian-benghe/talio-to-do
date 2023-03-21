package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

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

    /**
     * This method initializes this controller instances
     * @param primaryStage an injection of the primary stage
     * @param mainOverview an injection of the MainOverview scene and controller
     * @param boardOverview an injection of the BoardOverview scene and controller
     * @param clientConnect an injection of the ClientConnect scene and controller
     * @param popupStage an injection of the PopupStage scene and controller
     * @param cardView an injection of the CardView scene and controller
     * @throws Exception an exception that may be thrown
     */

    public void initialize(Stage primaryStage,
                           Pair<MainOverviewCtrl, Parent> mainOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<DeleteBoardPopUpCtrl, Parent> popupStage,
                           Pair<CardViewCtrl, Parent> cardView) throws Exception {


        this.primaryStage = primaryStage;
        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.mainOverviewCtrl = mainOverview.getKey();
        this.mainOverview = new Scene(mainOverview.getValue());

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.cardViewCtrl = cardView.getKey();
        this.cardView = new Scene(cardView.getValue());
        //Set the primary stage to be not resizable
        primaryStage.setResizable(false);

        //Set the stage icon
        //TODO Replace the temporary icon
        primaryStage.getIcons().add(new Image("RandomIcon.png"));


        showClientConnect();
        primaryStage.show();
    }


    /**
     * The method changes the scene to the BoardOverview
     * @param text the name of the selected board
     */
    public void showBoardOverview(String text) {
        System.out.println(text);
        primaryStage.setTitle("Talio - Board View");
        primaryStage.setScene(boardOverview);
        primaryStage.centerOnScreen();
        boardOverviewCtrl.setBoardTitle(text);
    }

    /**
     * This method changes the scene to the MainOverview scene.
     */
    public void showMainOverview() {


        primaryStage.setTitle("Talio - Home");
        primaryStage.setScene(mainOverview);
        primaryStage.centerOnScreen();
        //Refresh the Scene
        mainOverviewCtrl.refreshOverview();


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
        primaryStage.setTitle("Talio - CardView");

        primaryStage.setScene(cardView);
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();
    }



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
    }


}