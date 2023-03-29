package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;


public class TagTemplateCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;




    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public TagTemplateCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }



}