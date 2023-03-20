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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

import commons.Board;
import commons.Card;
import commons.Column;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
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
     * getter for the
     * @return
     */
    public static String getServerAddress() {
        return server;
    }

    private static String getServerUrl(String path) {
        return server + path;
    }

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }


    /**
     * Adds a new card to the to-do list.
     * This method sends a POST request to the server to add a new card
     * to the to-do list. The card parameter should contain all necessary
     * information about the new card, including the task description,
     * due date, and priority level. If the card is successfully added,
     * the method returns an HTTP response with a status code of 200 (OK).
     * @param card The card to add to the list.
     * @return The HTTP response indicating whether the card was successfully added.
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    public List<Card> getCardsFromColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .queryParam("column_id", column.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    public Column addColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(column, APPLICATION_JSON), Column.class);
    }

    public List<Column> getColumns() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {});
    }

    public List<Column> getColumnsFromBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .queryParam("board_id", board.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {});
    }

    //this should update the board title when it's modified
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
    public Board getBoardById(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Board.class);
    }

}