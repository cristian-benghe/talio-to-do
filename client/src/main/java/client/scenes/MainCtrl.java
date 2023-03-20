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


    public void initialize(Stage primaryStage, Pair<MainOverviewCtrl, Parent> mainOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<DeleteBoardPopUpCtrl, Parent> popupStage) throws Exception {
        this.primaryStage = primaryStage;

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.mainOverviewCtrl = mainOverview.getKey();
        this.mainOverview = new Scene(mainOverview.getValue());

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        //Set the primary stage to be not resizable
        primaryStage.setResizable(false);

        //Set the stage icon
        //TODO Replace the temporary icon
        primaryStage.getIcons().add(new Image("RandomIcon.png"));


        showClientConnect();
        primaryStage.show();
    }

    public void showBoardOverview(String text) {
        System.out.println(text);
        primaryStage.setTitle("Talio - Board View");
        primaryStage.setScene(boardOverview);
        primaryStage.centerOnScreen();
        boardOverviewCtrl.setBoard_title(text);
    }

    public void showMainOverview() {


        primaryStage.setTitle("Talio - Home");
        primaryStage.setScene(mainOverview);
        primaryStage.centerOnScreen();
        //Refresh the Scene
        mainOverviewCtrl.refreshOverview();


    }

    public void showClientConnect() {
        primaryStage.setTitle("Talio - Connect to a Server");

        primaryStage.setScene(clientConnect);
        clientConnectCtrl.refresh();
        primaryStage.centerOnScreen();
        //clientConnectCtrl.connect();              //This line seems irrelevant. Why attempt to connect without any user-approved url?
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


}