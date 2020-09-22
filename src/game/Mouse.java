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

import java.awt.Graphics;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Mouse extends Entity {

    public Snake snake;
    public Apple apple;

    public Mouse(Snake sn, Apple ap, int sSize, int rWidth, int rHeight) {
        ticksStarve = 0;
        ticksNeedToStarve = 60;
        ticksNeedToMove = 4;
        staminaMax = 10;
        stamina = staminaMax;
        staminaStep = 3;
        squareSize = sSize;
        roomWidth = rWidth;
        roomHeight = rHeight;
        snake = sn;
        apple = ap;
        x = rand.nextInt(roomWidth / squareSize) * squareSize;
        y = rand.nextInt(roomHeight / squareSize) * squareSize + squareSize;
    }

    public void generate() {
        stamina = 10;
        int newx = rand.nextInt(roomWidth / squareSize) * squareSize;
        int newy = rand.nextInt(roomHeight / squareSize) * squareSize + squareSize;

        while ((newx == x) && (!snake.checkBodyCollision(newx, newy))) {
            newx = rand.nextInt(roomWidth / squareSize) * squareSize;
        }
        while ((newy == y) && (!snake.checkBodyCollision(newx, newy))) {
            newy = rand.nextInt(roomHeight / squareSize) * squareSize + squareSize;
        }
        x = newx;
        y = newy;
    }

    @Override
    public void render(Graphics g, int scale) {
        g.setColor(Colors.BLUE);
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
            int moveX = 0;
            int moveY = 0;
            if (rand.nextInt(2) == 0) {
                if (apple.x > x) {
                    moveX = 1 * squareSize;
                }
                if (apple.x < x) {
                    moveX = -1 * squareSize;
                }
                if ((x + moveX >= 0) && (x + moveX < roomWidth)) {
                    if (!snake.checkBodyCollision(x + moveX, y)) {
                        x = x + moveX;
                    }
                }
            } else {
                if (apple.y > y) {
                    moveY = 1 * squareSize;
                }
                if (apple.y < y) {
                    moveY = -1 * squareSize;
                }

                if ((y + moveY > 0) && (y + moveY < roomHeight)) {
                    if (!snake.checkBodyCollision(x, y + moveY)) {
                        y = y + moveY;
                    }
                }
            }
        }
    }

    public boolean checkBodyCollision(int xTarget, int yTarget) {
        return (xTarget == x) && (yTarget == y);
    }

    void eat() {
        stamina += staminaStep;
        if (stamina > staminaMax) {
            stamina = staminaMax;
        }
    }
}
