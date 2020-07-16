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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Snake {

    public final int[] bodyX = new int[100];
    public final int[] bodyY = new int[100];
    public int len = 2;
    public final int ticksNeedToMove = 6;
    public int ticks = 0;
    public int currentSpeed = 0;
    public KeysInput input;

    public Snake(KeysInput keysInput) {
        input = keysInput;
        int i = 0;
        while (i < len) {
            bodyX[i] = i * 10;
            bodyY[i] = 20;
            i++;
        }
    }

    public void render(Graphics g, int scale) {
        int i = 0;
        while (i < len) {
            if (0 == i) {
                g.setColor(new Color(255, 0, 0, 255));

            } else {
                g.setColor(new Color(0, 255, 0, 255));
            }
            g.drawString("@", bodyX[i] * scale,
                    bodyY[i] * scale);
            i++;
        }
    }

    public void tick(int tick) {
        boolean move = false;
        ticks++;
        if (ticks > ticksNeedToMove) {
            move = true;
            ticks = 0;
        }

        if (move) {
            if (input.up.down) {
                if (bodyY[0] > 10) {

                    moveBody();
                    bodyY[0] -= 10;

                }
                return;
            }
            if (input.down.down) {
                if (bodyY[0] < 200) {
                    moveBody();
                    bodyY[0] += 10;
                }
                return;
            }
            if (input.left.down) {
                if (bodyX[0] > 0) {
                    moveBody();
                    bodyX[0] -= 10;
                }
                return;
            }
            if (input.right.down) {
                if (bodyX[0] < 310) {
                    moveBody();
                    bodyX[0] += 10;
                }
            }
        }
    }

    public void moveBody() {
        for (int i = len - 1; i > 0; i--) {
            if (bodyX[i] != bodyX[i - 1]){
            bodyX[i] = bodyX[i - 1];}
            
            if(bodyY[i] != bodyY[i - 1]){
                bodyY[i] = bodyY[i - 1];
            }
        }
    }
}
