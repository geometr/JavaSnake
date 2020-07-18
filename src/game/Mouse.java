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
import java.util.Random;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Mouse extends Entity{

    private final Random rand = new Random();
    public int ticks = 0;
    public int ticksNeedToMove = 4;
    public int stamina = 10;
    public Snake snake;
    public Apple apple;
    public int ticksNeedToStarve = 60;
    public int ticksStarve = 0;

    public Mouse(Snake sn, Apple ap) {
        snake = sn;
        apple = ap;
        x = rand.nextInt(32) * 10;
        y = rand.nextInt(19) * 10 + 10;
    }

    public void generate() {
        stamina = 10;
        int newx = rand.nextInt(32) * 10;
        int newy = rand.nextInt(19) * 10 + 10;

        while ((newx == x) && (!snake.checkBodyCollision(newx, newy))) {
            newx = rand.nextInt(32) * 10;
        }
        while ((newy == y) && (!snake.checkBodyCollision(newx, newy))) {
            newy = rand.nextInt(19) * 10 + 10;
        }
        x = newx;
        y = newy;
    }

    @Override
    public void render(Graphics g, int scale) {
        g.setColor(new Color(0, 0, 255, 255));
        g.drawString("X", x * scale, y * scale);
    }

    @Override
    public void tick(int tick) {
        boolean move = false;
        ticksStarve++;
        if (ticksStarve > ticksNeedToStarve) {
            ticksStarve = 0;
            if (stamina > 0) {
                stamina--;
            }
        }
        ticks++;
        if (ticks > ticksNeedToMove) {
            move = true;
            ticks = 0;
        }
        if (move && (stamina > 0)) {
            int moveX =0;
            int moveY =0;
            if (rand.nextInt(2) == 0) {
                if (apple.x > x) {
                    moveX = 1*10;
                }
                if (apple.x < x) {
                    moveX = -1*10;
                }
                if ((x + moveX >= 0) && (x + moveX < 320)) {
                    if (!snake.checkBodyCollision(x + moveX, y)) {
                        x = x + moveX;
                    }
                }
            } else {
                if (apple.y > y) {
                    moveY = 1*10;
                }
                if (apple.y < y) {
                    moveY = -1*10;
                }

                if ((y + moveY > 0) && (y + moveY < 200)) {
                    if (!snake.checkBodyCollision(x, y + moveY)) {
                        y = y + moveY;
                    }
                }
            }
        }
    }

    public boolean checkBodyCollision(int xTarget, int yTarget) {

        if ((xTarget == x) && (yTarget == y)) {
            return true;
        }
        return false;
    }

    void eat() {
        stamina += 3;
        if (stamina > 10) {
            stamina = 10;
        }
    }
}
