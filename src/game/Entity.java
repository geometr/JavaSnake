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
import java.util.Random;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
abstract public class Entity {

    public int x;
    public int y;
    public final Random rand = new Random();
    
    public void render(Graphics g, int scale){
        
    };
    public void tick(int tick) {

    }
}
