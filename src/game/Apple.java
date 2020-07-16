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
public class Apple {

    public int x;
    public int y;
    private final Random rand = new Random();

    public Apple() {
        x = rand.nextInt(32) * 10;
        y = rand.nextInt(19) * 10+10;
    }
    public void generate() {
        int newx = rand.nextInt(32) * 10;
        int newy = rand.nextInt(19) * 10+10;
        while (newx == x){
             newx = rand.nextInt(32) * 10;
        }    
        while (newy == y){
             newy = rand.nextInt(19) * 10+10;
        }  
        x= newx;
        y= newy;
    }

    public void render(Graphics g,int scale) {
        g.setColor(new Color(0, 0, 255, 255));
        g.drawString("@", x*scale, y * scale);
    }
}
