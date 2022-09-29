package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void check_toggle_down_true() {
        Key actualKey1 = new Key();
        Key actualKey2 = new Key();

        actualKey1.toggle(true);
        actualKey2.toggle(true);
        actualKey2.toggle(true);

        assertTrue(actualKey1.press());
        assertTrue(actualKey2.press());
    }

    @Test
    void check_toggle_down_false() {
        Key actualKey1 = new Key();
        Key actualKey2 = new Key();

        actualKey1.toggle(true);
        actualKey1.toggle(false);
        actualKey2.toggle(false);

        assertFalse(actualKey1.press());
        assertFalse(actualKey2.press());
    }

    @Test
    void check_toggle_tick(){
        Key actualKey1 = new Key();
        Key actualKey2 = new Key();
        Key actualKey3 = new Key();

        actualKey1.tick();
        actualKey2.toggle(true);
        actualKey2.tick();
        actualKey3.toggle(true);
        actualKey3.tick();
        actualKey3.toggle(false);
        actualKey3.tick();
        actualKey3.tick();

        assertFalse(actualKey1.isClicked());
        assertTrue(actualKey2.isClicked());
        assertFalse(actualKey3.isClicked());
    }
}