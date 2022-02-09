/*
 * Copyright (C) 2020 Kurochkin Konstantin <geometr.sinc@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class KeysInput implements KeyListener {

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key escape = new Key();
    public Key enter = new Key();

    KeysInput(Client client) {
        client.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        toggle(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggle(e, false);
    }

    private void toggle(KeyEvent ke, boolean pressed) {
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            up.toggle(pressed);
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) {
            down.toggle(pressed);
        }
        if (ke.getKeyCode() == KeyEvent.VK_A) {
            left.toggle(pressed);
        }
        if (ke.getKeyCode() == KeyEvent.VK_D) {
            right.toggle(pressed);
        }
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escape.toggle(pressed);
        }
        if (ke.getKeyCode() == KeyEvent.VK_ENTER){
            enter.toggle(pressed);
        }
    }

    public void releaseAll() {
        up.down = false;
        down.down = false;
        left.down = false;
        right.down = false;
        escape.down = false;
        enter.down = false;
    }
}
