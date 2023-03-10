package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showBoardOverview();
    }
}
