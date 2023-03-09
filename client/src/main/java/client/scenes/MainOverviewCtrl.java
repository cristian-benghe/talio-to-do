package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class MainOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MainOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showMainOverview();
    }
}
