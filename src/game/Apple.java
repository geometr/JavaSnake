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
public class Apple extends Entity {


    public Apple(int sSize, int rWidth, int rHeight) {
        squareSize = sSize;
        roomWidth = rWidth;
        roomHeight = rHeight;
        generate();
    }

    public void generate() {
        int newx = rand.nextInt(roomWidth / squareSize) * squareSize;
        int newy = rand.nextInt(roomHeight / squareSize - 1) * squareSize + squareSize;

        while (newx == x) {
            newx = rand.nextInt(roomWidth / squareSize) * squareSize;
        }
        while (newy == y) {
            newy = rand.nextInt(roomHeight / squareSize - 1) * squareSize + squareSize;
        }
        x = newx;
        y = newy;
    }

    @Override
    public void render(Graphics g, int scale) {
        g.setColor(Colors.YELLOW);
        g.drawString("*", x * scale, y * scale);
    }

}
