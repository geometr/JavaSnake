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
public class Snake {

    public static final int MAX_SNAKE_LEN = 255;
    public static final int START_SNAKE_LEN = 6;
    public static final int HEAD = 0;
   
    public final int[] bodyX = new int[MAX_SNAKE_LEN];
    public final int[] bodyY = new int[MAX_SNAKE_LEN];
    public int len = START_SNAKE_LEN;
    public int ticksNeedToMove = 6;
    public int ticksNeedToStarve = 120;
    public int ticksStarve = 0;
    public int ticks = 0;
    public int currentSpeed = 0;
    public KeysInput input;
    public int squareSize;
    public int roomWidth;
    public int roomHeight;
    public int staminaMax = 10;
    public int stamina = staminaMax;
    public int staminaStep = 3;

    public Snake(KeysInput keysInput, int sSize, int rWidth, int rHeight) {
        roomWidth = rWidth;
        roomHeight = rHeight;
        squareSize = sSize;
        input = keysInput;
        int i = 1;
        while (i <= len) {
            bodyX[len - i] = (i + roomHeight / squareSize / 2 ) * squareSize;
            bodyY[len - i] = roomHeight / squareSize / 2 * squareSize;
            i++;
        }
    }

    public void render(Graphics g, int scale) {
        int i = len - 1;
        while (i >= HEAD) {
            if (HEAD == i) {
                g.setColor(Colors.RED);

            } else {
                g.setColor(Colors.GREEN);
            }
            g.drawString("@", bodyX[i] * scale,
                    bodyY[i] * scale);
            i--;
        }
        for (i = 0; i < stamina; i++) {
            g.setColor(Colors.RED);
            g.drawString("♥", i * squareSize * scale, roomHeight * scale);
        }
    }

    public void tick(int tick) {
        boolean move = false;
        ticks++;
        ticksStarve++;
        if (ticksStarve > (ticksNeedToStarve - len)) {
            ticksStarve = 0;
            if (stamina > 0) {
                stamina--;
            }
        }
        if (ticks > ticksNeedToMove) {
            move = true;
            ticks = 0;
        }

        if (move) {
            if (input.up.down) {
                if (bodyY[HEAD] > squareSize) {
                    if (!checkBodyCollision(bodyX[HEAD], bodyY[HEAD] - squareSize)) {
                        moveBody();
                        bodyY[HEAD] -= squareSize;
                    }
                }
                return;
            }
            if (input.down.down) {
                if (bodyY[HEAD] < roomHeight - squareSize) {
                    if (!checkBodyCollision(bodyX[HEAD], bodyY[HEAD] + squareSize)) {
                        moveBody();
                        bodyY[HEAD] += squareSize;
                    }
                }
                return;
            }
            if (input.left.down) {
                if (bodyX[HEAD] > 0) {
                    if (!checkBodyCollision(bodyX[HEAD] - squareSize, bodyY[HEAD])) {
                        moveBody();
                        bodyX[HEAD] -= squareSize;
                    }
                }
                return;
            }
            if (input.right.down) {
                if (bodyX[HEAD] < roomWidth - squareSize) {
                    if (!checkBodyCollision(bodyX[HEAD] + squareSize, bodyY[HEAD])) {
                        moveBody();
                        bodyX[HEAD] += squareSize;
                    }
                }
            }
        }
    }

    public void moveBody() {
        for (int i = len - 1; i > HEAD; i--) {
            bodyX[i] = bodyX[i - 1];
            bodyY[i] = bodyY[i - 1];
        }
    }

    public void growAndEat() {
        len++;
        stamina += staminaStep;
        if (stamina > staminaMax) {
            stamina = staminaMax;
        }
        bodyX[len - 1] = bodyX[len - 2];
        bodyY[len - 1] = bodyY[len - 2];
    }

    public boolean checkHeadCollision(int x, int y) {
        boolean collision = false;
        if ((bodyX[HEAD] == x) && (bodyY[HEAD] == y)) {
            collision = true;
        }
        return collision;
    }

    public boolean checkBodyCollision(int x, int y) {

        for (int i = 0; i < len; i++) {
            if ((bodyX[i] == x) && (bodyY[i] == y)) {
                return true;
            }
        }
        return false;
    }
}
