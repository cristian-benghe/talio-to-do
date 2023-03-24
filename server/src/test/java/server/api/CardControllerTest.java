//package server.api;
//
//
//import static org.junit.jupiter.api.Assertions.*;
////import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
////import static org.mockito.ArgumentMatchers.any;
////import static org.mockito.Mockito.verify;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import commons.Card;
////import commons.Column;
//import server.database.CardRepository;
//import server.database.ColumnRepository;
//
//class CardControllerTest {
//
//    @Mock
//    private CardRepository cardRepo;
//
//    @Mock
//    private ColumnRepository columnRepo;
//
//    @InjectMocks
//    private CardController cardController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    /**
//     * Tests if the GET request successfully returns all the cards in the database
//     */
//    @Test
//    void testGetAll() {
//        List<Card> cards = new ArrayList<>();
//        cards.add(new Card("Card 1", "Description 1", null, null));
//        cards.add(new Card("Card 2", "Description 2", null, null));
//        when(cardRepo.findAll()).thenReturn(cards);
//
//        List<Card> actualCards = cardController.getAll();
//        assertEquals(cards, actualCards);
//    }
//    /**
//     * Tests if the GET (by ID) request successfully returns OK (status 200)
//     * if there is a card with that ID in the database
//     */
//    @Test
//    void testGetByIdWithValidId() {
//        long id = 1L;
//        Card card = new Card("Card 1", "Description 1", null, null);
//        when(cardRepo.existsById(id)).thenReturn(true);
//        when(cardRepo.findById(id)).thenReturn(Optional.of(card));
//
//        ResponseEntity<Card> response = cardController.getById(id);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(card, response.getBody());
//    }
//    /**
//     * Tests if the GET (by ID) request successfully returns BAD REQUEST (error 400)
//     * if there is no card with that ID in the database
//     */
//
//    @Test
//    void testGetByIdWithInvalidId() {
//        long id = -1L;
//        when(cardRepo.existsById(id)).thenReturn(false);
//
//        ResponseEntity<Card> response = cardController.getById(id);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//    /**
//     * Tests if the POST request successfully adds a card to the database (status 200)
//     */
//
//    @Test
//    void testAddWithValidCard() {
//        Card card = new Card("Card 1", "Description 1", null, null);
//        when(cardRepo.save(card)).thenReturn(card);
//
//        ResponseEntity<Card> response = cardController.add(card);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(card, response.getBody());
//    }
//    /**
//     * Tests if the POST request successfully returns BAD REQUEST
//     * (error 400) if the card has an empty value as a title
//     */
//
//    @Test
//    void testAddWithInvalidTitle() {
//        Card card = new Card(null, "Description 1", null, null);
//
//        ResponseEntity<Card> response = cardController.add(card);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
////    /**
////     * Tests the update method of the CardController class with a valid id and card.
////     */
////    @Test
////    void testUpdateWithValidIdAndCard() {
////        long id = 1L;
////        Card existingCard = new Card("Card 1", "Description 1", null, null);
////        Card updatedCard = new Card("Card 1 Updated", "Description 1 Updated", new ArrayList<>(), new HashSet<>());
////        when(cardRepo.findById(id)).thenReturn(Optional.of(existingCard));
////        when(cardRepo.save(existingCard)).thenReturn(existingCard);
////
////        ResponseEntity<Card> response = cardController.update(id, updatedCard);
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(existingCard, response.getBody());
////        assertEquals("Card 1 Updated", existingCard.getTitle());
////        assertEquals("Description 1 Updated", existingCard.getDescription());
////    }
////    /**
////     * Tests the successful update of a card's column.
////     * It verifies that the response has HTTP status code 200 OK and that the updated card's column matches the expected column.
////     */
////    @Test
////    public void updateColumnWhenCardAndColumnExist() {
////        long id = 1L;
////        long columnId = 2L;
////        Card card = new Card();
////        card.setId(id);
////        Column column = new Column();
////        column.setId(columnId);
////        when(cardRepo.findById(id)).thenReturn(Optional.of(card));
////        when(columnRepo.findById(columnId)).thenReturn(Optional.of(column));
////        Card updatedCard = new Card();
////        updatedCard.setId(id);
////        updatedCard.setColumn(column);
////        when(cardRepo.save(any(Card.class))).thenReturn(updatedCard);
////
////        CardController controller = new CardController(cardRepo, columnRepo);
////        ResponseEntity<Card> response = controller.updateColumn(id, columnId);
////
////        verify(cardRepo).findById(id);
////        verify(columnRepo).findById(columnId);
////        verify(cardRepo).save(any(Card.class));
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(updatedCard, response.getBody());
////    }
//    /**
//     * Tests that a card cannot be updated with an invalid column ID.
//     * It verifies that the response has HTTP status code 404 Not Found.
//     */
////    @Test
////    public void updateColumn_returnsNotFound_whenCardOrColumnDoesNotExist() {
////        long id = 1L;
////        long columnId = 2L;
////        when(cardRepo.findById(id)).thenReturn(Optional.empty());
////        when(columnRepo.findById(columnId)).thenReturn(Optional.empty());
////
////        CardController controller = new CardController(cardRepo, columnRepo);
////        ResponseEntity<Card> response = controller.updateColumn(id, columnId);
////
////        verify(cardRepo).findById(id);
////        verify(columnRepo).findById(columnId);
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////        assertNull(response.getBody());
////    }
//
//
//
//    /**
//     * Tests if the DELETE request successfully deletes the card and returns OK (status 200)
//     * if the card exists in the database
//     */
//    @Test
//    public void testDeleteCard() {
//        Card existingCard = new Card("Test Card", "This is a test card",
//                new ArrayList<>(), new HashSet<>());
//        existingCard.setId(1L);
//        when(cardRepo.findById(1L)).thenReturn(Optional.of(existingCard));
//        ResponseEntity<Void> response = cardController.delete(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    /**
//     * Tests if the DELETE request successfully returns NOT FOUND (error 404)
//     * if the card does not exist in the database
//     */
//    @Test
//    public void testDeleteBoardNotFound() {
//        long id = 1;
//        when(cardRepo.existsById(id)).thenReturn(false);
//        ResponseEntity<Void> response = cardController.delete(id);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}