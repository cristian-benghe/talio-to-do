package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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


    @Test
    public void testDefaultConstructor() {
        // Create a task object using the default constructor
        Task task = new Task();

        // Check that the properties of the object have their default values
        assertEquals(0, task.getID());
        assertEquals(0, task.getPosition());
        assertEquals(null, task.getTitle());
        assertEquals(false, task.getStatus());
    }

    @Test
    public void testToString() {
        // Create a task object with some property values
        Task task = new Task(1, 1, "title1", true);

        // Check that the toString() method returns the expected string
        assertEquals("Task{task_id=1, position=1, title='title1', isComplete=true}", task.toString());
    }
    @Test
    public void testHashCode() {
        // Create two objects with identical property values
        Object obj1 = new Task(1, 1, "title1", true);
        Object obj2 = new Task(1, 1, "title1", true);

        // Check that their hash codes are equal
        assertEquals(obj1.hashCode(), obj2.hashCode());

        // Create two objects with different property values
        Object obj3 = new Task(2, 2, "title2", true);
        Object obj4 = new Task(3, 3, "title3", false);

        // Check that their hash codes are not equal
        assertNotEquals(obj3.hashCode(), obj4.hashCode());
    }

    @Test
    void setPosition() {
        Task t1 = new Task(t);
        t1.setPosition(3);
        assertNotEquals(3,t.getPosition());
    }
}