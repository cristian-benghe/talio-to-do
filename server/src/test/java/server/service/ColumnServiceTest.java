package server.service;

import commons.Card;
import commons.Column;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.CardRepository;
import server.database.ColumnRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ColumnServiceTest {


    private ColumnRepository columnRepo;

    private CardRepository cardRepo;

    private ColumnService columnService;

    private Column column;
    private Card card1;
    private Card card2;
    private List<Card> cards;

    @BeforeEach
    public void setup() {
        columnRepo = mock(ColumnRepository.class);
        cardRepo = mock(CardRepository.class);
        columnService = new ColumnService(columnRepo, cardRepo);

        MockitoAnnotations.openMocks(this);
        column = new Column("column title", new ArrayList<>());
        card1 = new Card("card title 1", "card desc 1", new ArrayList<>(), new HashSet<>());
        card1.setId(1L);
        card2 = new Card("card title 2", "card desc 2", new ArrayList<>(), new HashSet<>());
        card2.setId(2L);
        cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        column.setId(1L);
        column.setCards(cards);
    }

    @Test
    public void testGetAll() {
        List<Column> columns = new ArrayList<>();
        Column column1 = new Column("Column 1", null);
        Column column2 = new Column("Column 2", null);
        columns.add(column1);
        columns.add(column2);
        when(columnRepo.findAll()).thenReturn(columns);

        List<Column> result = columnService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).contains(column1, column2);
    }

    @Test
    public void testGetById() {
        long columnId = 1L;
        Column column = new Column("Column 1", null);
        column.setId(columnId);
        when(columnRepo.findById(anyLong())).thenReturn(Optional.of(column));

        Optional<Column> result = columnService.getById(columnId);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(columnId);
        assertThat(result.get().getTitle()).isEqualTo(column.getTitle());
    }

    @Test
    public void testAdd() {
        Column column = new Column("Column 1", null);
        when(columnRepo.save(column)).thenReturn(column);

        Column result = columnService.add(column);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(column.getTitle());
    }

    @Test
    public void testAddWithNullTitle() {
        Column column = new Column(null, null);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            columnService.add(column);
        });
    }

    @Test
    public void testAddWithEmptyTitle() {
        Column column = new Column("", null);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            columnService.add(column);
        });
    }

    @Test
    public void testUpdate() {
        Column existing = new Column("Column 1", null);
        List<Card> existingCards = new ArrayList<>();
        Card card1 = new Card("Card 1", null, null, null);
        Card card2 = new Card("Card 2", null, null, null);
        existingCards.add(card1);
        existing.setCards(existingCards);

        Column updated = new Column("Column 2", null);
        List<Card> updatedCards = new ArrayList<>();
        updatedCards.add(card2);
        updated.setCards(updatedCards);

        when(columnRepo.save(existing)).thenReturn(existing);

        Column result = columnService.update(existing, updated);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updated.getTitle());
        assertThat(result.getCards()).containsExactly(card2);
    }

    @Test
    void testDeleteColumn() {
        long columnId = column.getId();

        when(columnRepo.findById(columnId)).thenReturn(Optional.of(column));

        boolean result = columnService.delete(columnId);

        assertTrue(result);
        verify(columnRepo,times(2)).deleteById(columnId);
    }

    @Test
    void testDeleteNonexistentColumn() {
        long columnId = column.getId();

        when(columnRepo.findById(columnId)).thenReturn(Optional.empty());

        boolean result = columnService.delete(columnId);

        assertFalse(result);
        verify(columnRepo).findById(columnId);
        verify(cardRepo, never()).deleteById(card1.getId());
        verify(cardRepo, never()).deleteById(card2.getId());
        verify(columnRepo, never()).deleteById(columnId);
    }
}