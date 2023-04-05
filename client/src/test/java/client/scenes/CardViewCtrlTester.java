package client.scenes;

import client.utils.ServerUtils;

public class CardViewCtrlTester extends CardViewCtrl{

    /**
     * constructor
     * @param server injected
     * @param mainCtrl injected
     */
    public CardViewCtrlTester(ServerUtils server, MainCtrl mainCtrl) {
        super(server, mainCtrl);
    }


    /**
     * will be used call displayTasks() in CardViewCtrl
     */
    @Override
    public void displayTasks() {

    }

    /**
     * will be used call displayTags() in CardViewCtrl
     */
    @Override
    public void displayTags() {

    }
}
