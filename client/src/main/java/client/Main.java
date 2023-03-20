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
package client;

import client.scenes.*;
import com.google.inject.Injector;
//import commons.Card;  //commented because of redlined
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var boardOverview = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "Board.fxml");
        var mainOverview = FXML.load(MainOverviewCtrl.class, "client", "scenes", "Home.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var clientCtrl = FXML.load(ClientConnectCtrl.class, "client", "scenes", "ClientConnect.fxml");
        var popUpStage = FXML.load(DeleteBoardPopUpCtrl.class, "client", "scenes", "DeleteBoard.fxml");
        var cardView = FXML.load(CardViewCtrl.class, "client", "scenes", "CardView.fxml");

        mainCtrl.initialize(primaryStage, overview, add, mainOverview, boardOverview, clientCtrl, popUpStage, cardView);
    }
}