package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {


    @Test
    public void getIDPositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertEquals(t.getID(),1L);

    }

    @Test
    public void getCardPositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        //This assertion won't function well if there is no equals() implementation in the Card class.
        assertEquals(t.getCard(),c);

    }

    @Test
    public void getTitlePositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertEquals(t.getTitle(),"Test Task");
    }

    @Test
    public void getStatusPositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertFalse(t.isComplete());
    }

    @Test
    public void setIDPositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertEquals(t.setID(0L),0L);
    }

    @Test
    public void setTitlePositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertEquals(t.setTitle("New Title"),"New Title");
    }

    @Test
    public void changeCompleteStatePositiveTest(){

        Card c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        Task t = new Task(1L,c,"Test Task",false);

        assertTrue(t.changeCompleteState());
    }
}