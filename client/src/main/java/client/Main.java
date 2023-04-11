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
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.application.Application;
//import javafx.scene.Parent;
import javafx.stage.Stage;
//import javafx.util.Pair;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private final ServerUtils serverUtils = INJECTOR.getInstance(ServerUtils.class);


    /**
     * launches the application
     * @param args =
     */

    public static void main(String[] args) {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this
     * application, onto which
     * the application scene can be set.
     * Applications may create other stages,
     *  if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        var boardOverview = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "Board.fxml");
        var mainOverview = FXML.load(MainOverviewCtrl.class, "client", "scenes", "Home.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var clientCtrl = FXML.load(ClientConnectCtrl.class,
                "client", "scenes", "ClientConnect.fxml");
        var cardView = FXML.load(CardViewCtrl.class, "client", "scenes", "CardView.fxml");
        var tagView = FXML.load(TagViewCtrl.class,
                "client", "scenes", "TagView.fxml");

        var colorManagement=FXML.load(ColorManagementCtrl.class,
                "client", "scenes", "ColorManagement.fxml");

        mainCtrl.initialize(primaryStage, mainOverview, boardOverview,
                clientCtrl, cardView, tagView, colorManagement);

    }

}
