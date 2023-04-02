package server.api;


import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.CardService;
import server.service.ColumnService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private ColumnService columnService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Tests if the GET request successfully returns all the cards in the database
     */
    @Test
    void testGetAll() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Card 1", "Description 1", null, null));
        cards.add(new Card("Card 2", "Description 2", null, null));
        when(cardService.getAll()).thenReturn(cards);

        List<Card> actualCards = cardController.getAll();
        assertEquals(cards, actualCards);
    }
    /**
     * Tests if the GET (by ID) request successfully returns OK (status 200)
     * if there is a card with that ID in the database
     */
    @Test
    void testGetByIdWithValidId() {
        long id = 1L;
        Card card = new Card("Card 1", "Description 1", null, null);
       // when(cardService.getById(id)).thenReturn(true);
        when(cardService.getById(id)).thenReturn(Optional.of(card));

        ResponseEntity<Card> response = cardController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
    }
    /**
     * Tests if the GET (by ID) request successfully returns BAD REQUEST (error 400)
     * if there is no card with that ID in the database
     */

    @Test
    void testGetByIdWithInvalidId() {
        long id = -1L;
        //when(cardService.getById(id)).thenReturn(false);

        ResponseEntity<Card> response = cardController.getById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    /**
     * Tests if the POST request successfully adds a card to the database (status 200)
     */

    @Test
    void testAddWithValidCard() {
        Card card = new Card("Card 1", "Description 1", null, null);
        when(cardService.add(card)).thenReturn(card);

        ResponseEntity<Card> response = cardController.add(card);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
    }
    /**
     * Tests if the POST request successfully returns BAD REQUEST
     * (error 400) if the card has an empty value as a title
     */

    @Test
    void testAddWithInvalidTitle() {
        Card card = new Card(null, "Description 1", null, null);

        ResponseEntity<Card> response = cardController.add(card);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
//    /**
//     * Tests the update method of the CardController class with a valid id and card.
//     */
//    @Test
//    void testUpdateWithValidIdAndCard() {
//        long id = 1L;
//        Card existingCard = new Card("Card 1", "Description 1", null, null);
//        Card updatedCard = new Card("Card 1 Updated", "Description 1 Updated", new ArrayList<>(), new HashSet<>());
//        when(cardService.getById(id)).thenReturn(Optional.of(existingCard));
//        when(cardService.add(existingCard)).thenReturn(existingCard);
//
//        ResponseEntity<Card> response = cardController.
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(existingCard, response.getBody());
//        assertEquals("Card 1 Updated", existingCard.getTitle());
//        assertEquals("Description 1 Updated", existingCard.getDescription());
//    }
//    /**
//     * Tests the successful update of a card's column.
//     * It verifies that the response has HTTP status code 200 OK and that the updated card's column matches the expected column.
//     */
//    @Test
//    public void updateColumnWhenCardAndColumnExist() {
//        long id = 1L;
//        long columnId = 2L;
//        Card card = new Card();
//        card.setId(id);
//        Column column = new Column();
//        column.setId(columnId);
//        when(cardService.getById(id)).thenReturn(Optional.of(card));
//        when(columnService.getById(columnId)).thenReturn(Optional.of(column));
//        Card updatedCard = new Card();
//        updatedCard.setId(id);
//        updatedCard.setColumn(column);
//        when(cardRepo.save(any(Card.class))).thenReturn(updatedCard);
//
//        CardController controller = new CardController(cardRepo, columnRepo);
//        ResponseEntity<Card> response = controller.updateColumn(id, columnId);
//
//        verify(cardRepo).findById(id);
//        verify(columnRepo).findById(columnId);
//        verify(cardRepo).save(any(Card.class));
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedCard, response.getBody());
//    }
    /**
     * Tests that a card cannot be updated with an invalid column ID.
     * It verifies that the response has HTTP status code 404 Not Found.
     */
//    @Test
//    public void updateColumn_returnsNotFound_whenCardOrColumnDoesNotExist() {
//        long id = 1L;
//        long columnId = 2L;
//        when(cardRepo.findById(id)).thenReturn(Optional.empty());
//        when(columnRepo.findById(columnId)).thenReturn(Optional.empty());
//
//        CardController controller = new CardController(cardRepo, columnRepo);
//        ResponseEntity<Card> response = controller.updateColumn(id, columnId);
//
//        verify(cardRepo).findById(id);
//        verify(columnRepo).findById(columnId);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNull(response.getBody());
//    }



    /**
     * Tests if the DELETE request successfully deletes the card and returns OK (status 200)
     * if the card exists in the database
     */
    @Test
    public void testDeleteCard() {
        Card existingCard = new Card("Test Card", "This is a test card",
                new ArrayList<>(), new HashSet<>());
        existingCard.setId(1L);
        when(cardService.getById(1L)).thenReturn(Optional.of(existingCard));
        ResponseEntity<Void> response = cardController.delete(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests if the DELETE request successfully returns NOT FOUND (error 404)
     * if the card does not exist in the database
     */
    @Test
    public void testDeleteBoardNotFound() {
        long id = 1;
        //when(cardService.getById(id)).thenReturn(false);
        ResponseEntity<Void> response = cardController.delete(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}