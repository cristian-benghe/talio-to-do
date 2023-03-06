package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {


    //Test variables
    Card c;
    Task t;

    @BeforeEach
    public void setup(){
        c = new Card("Test Card",null,null,null,null);
        c.setId(10L);

        t = new Task(1L,c,"Test Task",false);
    }
    @Test
    public void getIDPositiveTest(){
        assertEquals(t.getID(),1L);
    }

    @Test
    public void getCardPositiveTest(){
        //This assertion won't function well if there is no equals() implementation in the Card class.
        assertEquals(t.getCard(),c);

    }

    @Test
    public void getTitlePositiveTest(){
        assertEquals(t.getTitle(),"Test Task");
    }

    @Test
    public void getStatusPositiveTest(){
        assertFalse(t.isComplete());
    }

    @Test
    public void setIDPositiveTest(){
        assertEquals(t.setID(0L),0L);
    }

    @Test
    public void setTitlePositiveTest(){
        assertEquals(t.setTitle("New Title"),"New Title");
    }

    @Test
    public void changeCompleteStatePositiveTest(){
        assertTrue(t.changeCompleteState());
    }
}