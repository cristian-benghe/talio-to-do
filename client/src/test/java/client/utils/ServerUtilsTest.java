package client.utils;

import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class ServerUtilsTest {

    private ServerUtils serverUtils;
    private String invalidServerAddress = "http://invalid.server.address";

    @BeforeEach
    void setUp() {
        serverUtils = new ServerUtils();
    }

//    @Test
//    public void testGetBoardsWhenServerNotStarted() throws Exception {
//        // create a mock Response object
//        Response response = mock(Response.class);
//
//        // set up the mock response to return a HTTP 404 error
//        when(response.getStatus()).thenReturn(Response.Status.NOT_FOUND.getStatusCode());
//
//        // create a mock Client object
//        Client client = mock(Client.class);
//
//        // set up the mock client to return the mock response
//        when(client.target(anyString()).request(MediaType.APPLICATION_JSON_TYPE)
//                .get(eq(Response.class))).thenReturn(response);
//
//        // call the method being tested
//        List<Board> boards = serverUtils.getBoards();
//
//        // assert that the method returned an empty list
//        assertTrue(boards.isEmpty());
//    }
}
