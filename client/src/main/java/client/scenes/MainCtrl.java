package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private MainOverviewCtrl mainOverviewCtrl;
    private Scene mainOverview;


    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;

    private DeleteBoardPopUpCtrl deleteBoardPopUpCtrl;
    private Scene popupStage;


    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add, Pair<MainOverviewCtrl, Parent> mainOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                           Pair<ClientConnectCtrl, Parent> clientConnect,
                           Pair<DeleteBoardPopUpCtrl, Parent> popupStage) throws IOException {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.mainOverviewCtrl = mainOverview.getKey();
        this.mainOverview = new Scene(mainOverview.getValue());

        this.clientConnectCtrl = clientConnect.getKey();
        this.clientConnect = new Scene(clientConnect.getValue());

        this.deleteBoardPopUpCtrl = popupStage.getKey();
        this.popupStage = new Scene(popupStage.getValue());

        showClientConnect();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showBoardOverview(String text) {
        System.out.println(text);
        primaryStage.setTitle("Your Board");
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.setBoard_title(text);
    }

    public void showMainOverview() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(mainOverview);

        //Refresh the Scene
        mainOverviewCtrl.refreshOverview();
    }

    //TODO
    public void showClientConnect() {
        primaryStage.setTitle("Connect_client");
        primaryStage.setScene(clientConnect);
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
        deleteBoardPopUpCtrl.setText(title);
        deleteBoardPopUpCtrl.setID(id);
    }

}