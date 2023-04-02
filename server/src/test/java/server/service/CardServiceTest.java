package server.service;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

//    @Test
//    void testAddCardWithNullTitle() {
//        Card card = new Card(null, "desc1", null, null);
//
//        assertThatThrownBy(() -> cardService.add(card))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Title cannot be null or empty");
//
//        verify(cardRepository).save(card);
//    }

//    @Test
//    void testAddCardWithEmptyTitle() {
//        Card card = new Card("", "desc1", null, null);
//
//        assertThatThrownBy(() -> cardService.add(card))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Title cannot be null or empty");
//
//        verify(cardRepository).save(card);
//    }

    @Test
    void testDeleteCard() {
        cardService.delete(1);

        verify(cardRepository).deleteById(1L);
    }
}