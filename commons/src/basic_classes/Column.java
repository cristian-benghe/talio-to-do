package basic_classes;


import basic_classes.Card;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private String title;
    private List<Card>cards;

    public Column(String title, List<Card> cards) {
        this.title = title;
        this.cards = new ArrayList<>();
    }

}
