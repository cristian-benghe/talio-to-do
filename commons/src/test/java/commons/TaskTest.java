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

        t = new Task(1L,1,"Test Task",false);
    }

    @Test
    public void TaskInstanceConstructorNotNullTest(){
        Task t1 = new Task(t);
        assertNotNull(t1);
    }

    @Test
    public void NonIDAttributeConstructorNotNullTest(){
        Task t1 = new Task(2,"Test Task", false);
        assertNotNull(t1);
    }

    @Test
    public void IDAttributeConstructorNotNullTest(){
        assertNotNull(t);
    }


    @Test
    public void getIDPositiveTest(){
        assertEquals(t.getID(),1L);
    }


    @Test
    public void getTitlePositiveTest(){
        assertEquals(t.getTitle(),"Test Task");
    }

    @Test
    public void getStatusPositiveTest(){
        assertFalse(t.getStatus());
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
        assertTrue(t.setStatus(true));
    }

    @Test
    public void sameInstanceEqualityPositiveTest(){
        assertEquals(t,t);
    }

    @Test
    public void nullEqualityNegativeTest(){
        assertNotEquals(t,null);
    }

    @Test
    public void notSameClassEqualityNegativeTest(){
        assertNotEquals(t,"This is a REAL task.");
    }

    @Test
    public void notSameIDEqualityNegativeTest(){
        Task t1 = new Task(t);
        t1.setID(-100L);
        assertNotEquals(t,t1);
    }
    @Test
    public void notSameTitleEqualityNegativeTest(){
        Task t1 = new Task(t);
        t1.setTitle("Different Title");
        assertNotEquals(t,t1);
    }

    @Test
    public void notSameStatusEqualityNegativeTest(){
        Task t1 = new Task(t);
        t1.setStatus(true);
        assertNotEquals(t,t1);
    }


    @Test void sameAttributesEqualityPositiveTest(){
        Task t1 = new Task(1L,1,"Test Task",false);
        assertEquals(t,t1);
    }
}