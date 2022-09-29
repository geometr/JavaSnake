package game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    private Score sc;
    @BeforeEach
    void setUp() {
        sc = new Score(10,"Name");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getScore() {
        assertEquals(10, sc.getScore());
    }

    @Test
    void setScore() {
        sc.setScore(9999);

        assertEquals(9999, sc.getScore());
    }

    @Test
    void getName() {
        assertSame("Name", sc.getName());
    }

    @Test
    void setName() {
        sc.setName("NAME2");

        assertSame("NAME2",sc.getName());
        assertFalse("Name".equals(sc.getName()));
    }
}