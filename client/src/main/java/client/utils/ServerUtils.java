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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.NoSuchElementException;

import commons.Board;
import commons.Card;
import commons.Column;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String server;

    /**
     * set method used in the first scene of the application
     * @param serverAddress the string containing the URL
     */
    public static void setServerAddress(String serverAddress) {
        ServerUtils.server = serverAddress;
    }

    /**
     * getter for the server address
     * @return the string containing the URL
     */
    public static String getServerAddress() {
        return server;
    }

    /**
     * Returns an URL with a certain path added
     * @param path the desired path
     * @return the string containing the URL
     */
    private static String getServerUrl(String path) {
        return server + path;
    }

    /**
     * Adds a new board.
     * This method sends a POST request to the server to add a new card
     * @param board The board to be added to the list.
     * @return The deserialized board
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }


    /**
     * Retrieves an HTTP GET request for the boards resource.
     * @return a list of boards
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }


    /**
     * Adds a new card.
     * This method sends a POST request to the server to add a new card
     * to the to-do list. The card parameter should contain all necessary
     * information about the new card, including the task description.
     * @param card The card to add to the list.
     * @return The deserialized card.
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Retrieves an HTTP GET request for the cards resource.
     * @return all the cards in the database
     */
    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * Retrieves an HTTP GET request for the cards resource where the cards are in a certain column.
     * @param column the column we search cards in
     * @return a list of cards
     */
    public List<Card> getCardsFromColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .queryParam("column_id", column.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * Adds a new column.
     * This method sends a POST request to the server to add a new card
     * to the to-do list. The column parameter should contain all necessary
     * information about the new column
     * @param column The column to add to the list.
     * @return returns a Column object, which is the server's representation
     * of the newly created column. The response body from the server is
     * deserialized into a Column object using the Jackson JSON library,
     * and this object is returned to the caller of the addColumn method
     */
    public Column addColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(column, APPLICATION_JSON), Column.class);
    }

    /**
     * Retrieves an HTTP GET request for the columns resource.
     * @return all the columns in the database
     */
    public List<Column> getColumns() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {});
    }

    /**
     * Retrieves an HTTP GET request for the columns resource
     * where the columns are in a certain board.
     * @param board the board in which we are looking for columns
     * @return a list of cards
     */
    public List<Column> getColumnsFromBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .queryParam("board_id", board.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {});
    }

    /**
     * update the board title when it's modified
     * @param boardId the id of the board to be modified
     * @param newTitle the new title the board will have
     * @return The deserialized Board object
     */
    public Board updateBoardTitle(long boardId, String newTitle) {
        Board board = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .get(Board.class);
        if (board == null) {
            throw new NoSuchElementException();
        }
        board.setTitle(newTitle);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * This method sends an HTTP DELETE request using the Jersey client library
     * @param boardId - the id of the board that has to be deleted
     */
    public void deleteBoard(long boardId) {
        ClientBuilder.newClient(new ClientConfig()) // creates a new client
                .target(server) // sets the target server for the request
                .path("api/boards/" + boardId) // specifies the API endpoint to delete the board
                .request() // creates a new request object
                .delete(); // sends the HTTP DELETE request and returns the response,
        // but the code does not handle the response explicitly
    }

    /**
     * Retrieves using an HTTP GET request a board with a certain id
     * @param boardId the id of the board we are looking for
     * @return The deserialized Board object
     */
    public Board getBoardById(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Board.class);
    }

}