package server.service;

import commons.Card;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceTest {

    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        MockitoAnnotations.initMocks(this);
        cardService = new CardService(cardRepository);
    }

    @Test
    void testGetAllCards() {
        List<Card> cards = new ArrayList<Card>();
        cards.add(new Card("card1", null, null, null));
        cards.add(new Card("card2", null, null, null));

        when(cardRepository.findAll()).thenReturn(cards);

        List<Card> result = cardService.getAll();

        assertThat(result).isEqualTo(cards);
        verify(cardRepository).findAll();
    }

    @Test
    void testGetCardById() {
        Card card = new Card("card1", null, null, null);
        Optional<Card> optionalCard = Optional.of(card);

        when(cardRepository.findById(anyLong())).thenReturn(optionalCard);

        Optional<Card> result = cardService.getById(1);

        assertThat(result).isEqualTo(optionalCard);
        verify(cardRepository).findById(1L);
    }

    @Test
    void testGetCardByIdNotFound() {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Card> result = cardService.getById(1);

        assertThat(result).isEmpty();
        verify(cardRepository).findById(1L);
    }

    @Test
    void testAddCard() {
        Card card = new Card("card1", null, null, null);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card result = cardService.add(card);

        assertThat(result).isEqualTo(card);
        verify(cardRepository).save(card);
    }

    @Test
    void testAddCardWithNullTitle() {
        Card card = new Card(null, "desc1", null, null);

        assertThatThrownBy(() -> cardService.add(card))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title cannot be null or empty");

    }

    @Test
    void testAddCardWithEmptyTitle() {
        Card card = new Card("", "desc1", null, null);

        assertThatThrownBy(() -> cardService.add(card))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title cannot be null or empty");

    }

    @Test
    void testDeleteCard() {
        cardService.delete(1);

        verify(cardRepository).deleteById(1L);
    }


    @Test
    public void testUpdateExistingCard() {
        // Arrange
        long id = 1L;
        Card existingCard = new Card("", "desc1", null, null);
        existingCard.setId(id);
        Card updatedCard = new Card("updated", "desc1", null, null);
        updatedCard.setId(id);

        Mockito.when(cardRepository.findById(id)).thenReturn(Optional.of(existingCard));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(updatedCard);

        CardService cardService = new CardService(cardRepository);

        // Act
        Card result = cardService.update(id, updatedCard);

        // Assert
        assertEquals(updatedCard.getTitle(), result.getTitle());
        assertEquals(updatedCard.getBlue(), result.getBlue());
        assertEquals(updatedCard.getGreen(), result.getGreen());
        assertEquals(updatedCard.getRed(), result.getRed());
        assertEquals(updatedCard.getTaskList(), result.getTaskList());
    }

    @Test
    public void testUpdateNonExistingCard() {
        // Arrange
        assertThrows(IllegalArgumentException.class, () -> {
            long id = 1L;
            Card updatedCard = new Card("", "desc1", null, null);

            Mockito.when(cardRepository.findById(id)).thenReturn(Optional.empty());

            CardService cardService = new CardService(cardRepository);

            // Act
            cardService.update(id, updatedCard);
        });
    }

    @Test
    public void testAddTask() {
        // Arrange
        long cardId = 1L;
        Card existingCard = new Card("", "desc1", null, null);
        existingCard.setId(cardId);
        Task task = new Task(0, "task title", false);

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(existingCard);

        CardService cardService = new CardService(cardRepository);

        // Act
        Card result = cardService.addTask(cardId, task);

        // Assert
        assertEquals(1, result.getTaskList().size());
        assertEquals(task.getTitle(), result.getTaskList().get(0).getTitle());
    }

    @Test
    public void testAddTaskToNonExistingCard() {
        // Arrange
        assertThrows(IllegalArgumentException.class, () -> {

            long cardId = 1L;
            Task task = new Task(0, "task title", false);

            Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

            CardService cardService = new CardService(cardRepository);

            // Act
            cardService.addTask(cardId, task);

        });
    }

    @Test
    public void testUpdateTaskList() {
        // Arrange
        long cardId = 1L;
        Card existingCard = new Card("", "desc1", null, null);
        existingCard.setId(cardId);
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(0, "task title", false));
        taskList.add(new Task(1, "task title", false));

        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(existingCard);

        CardService cardService = new CardService(cardRepository);

        // Act
        Card result = cardService.updateTaskList(cardId, taskList);

        // Assert
        assertEquals(taskList, result.getTaskList());
    }
}