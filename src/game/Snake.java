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

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Snake {

    public final int[] bodyX = new int[100];
    public final int[] bodyY = new int[100];
    final int len = 12;
    public final int maxSpeed = 200;
    public int ticks = 0;
    public int currentSpeed = 0;

    public Snake() {
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
            if (len - 1 == i) {
                g.setColor(new Color(255, 0, 0, 255));

            } else {
                g.setColor(new Color(0, 255, 0, 255));
            }
            g.drawString("@", bodyX[i] * scale,
                    bodyY[i] * scale);
            i++;
        }
    }
}
