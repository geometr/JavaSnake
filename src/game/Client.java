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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Client extends Canvas implements Runnable, KeyListener {

    private static final int CLIENT_WIDTH = 320;
    private static final int CLIENT_HEIGHT = 200;
    private static final int CLIENT_SCALE = 5;
    private static final int TARGET_FPS = 100;

    private static int CLScale = CLIENT_SCALE;
    private static int CLWidth = CLIENT_WIDTH * CLIENT_SCALE;
    private static int CLHeight = CLIENT_HEIGHT * CLIENT_SCALE;
    private BufferStrategy bs;

    private final int[] snakeBodyX = new int[100];
    private final int[] snakeBodyY = new int[100];
    private final int snakeLen = 32;
    private final int appleX;
    private final int appleY;
    private final Random rand = new Random();

    private Key up = new Key();
    private Key down = new Key();
    private Key left = new Key();
    private Key right = new Key();

    public List<Key> keys = new ArrayList<>();

    private static void setupClientWindowHeightAndWidth() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int GDWidth = gd.getDisplayMode().getWidth();
        int GDHeight = gd.getDisplayMode().getHeight();
        while ((CLWidth > GDWidth) && ((CLHeight > GDHeight))) {
            CLScale--;
            CLWidth = CLIENT_WIDTH * CLScale;
            CLHeight = CLIENT_HEIGHT * CLScale;
        }
    }

    private Client() {
        int i = 0;
        while (i < snakeLen) {
            snakeBodyX[i] = i * 10;
            snakeBodyY[i] = 20;
            i++;
        }
        appleX = rand.nextInt(32) * 10;
        appleY = rand.nextInt(32) * 10;
        keys.add(up);
        keys.add(down);
        keys.add(left);
        keys.add(right);

    }

    public static void main(String[] args) {

        Client client = new Client();
        setupClientWindowHeightAndWidth();
        Dimension clientDimension = new Dimension(CLWidth, CLHeight);

        client.setMaximumSize(clientDimension);
        client.setMinimumSize(clientDimension);
        client.setPreferredSize(clientDimension);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Java Snake No libgdx sample");
        frame.setSize(CLWidth, CLHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(client, BorderLayout.CENTER);
        frame.pack();
        client.start();
    }

    @Override
    public void run() {
        long previous = System.currentTimeMillis();
        long second = System.currentTimeMillis();
        long lag = 0;
        long millisPerUpdate = 1000 / TARGET_FPS;
        int ticks = 0;
        int FPS = 0;
        int EPS = 0;
        int eticks = 0;

        while (true) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            processInput();
            while (lag >= millisPerUpdate) {
                lag -= millisPerUpdate;
                update();
                eticks++;
            }
            if (current - second >= 1000) {
                FPS = ticks;
                EPS = eticks;
                ticks = 0;
                second = current;
                eticks = 0;
            }
            ticks++;
            render(FPS, EPS);
        }
    }

    private void start() {
        bs = getBufferStrategy();
        while (null == bs) {
            createBufferStrategy(3);
            requestFocus();
            bs = getBufferStrategy();
        }
        new Thread(this).start();
    }

    private void render(int FPS, int EPS) {
        Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(0, 0, 0, 255));
        g.fillRect(0, 0, CLWidth, CLHeight);
        Font font = new Font("TimesRoman", Font.PLAIN, 11 * CLScale);
        g.setColor(new Color(255, 255, 255, 255));
        g.setFont(font);
        g.drawString("FPS " + FPS + " EPS " + EPS, 11 * CLScale, 11 * CLScale);
        int i = 0;
        while (i < snakeLen) {
            if (snakeLen - 1 == i) {
                g.setColor(new Color(255, 0, 0, 255));

            } else {
                g.setColor(new Color(0, 255, 0, 255));
            }
            g.drawString("@", this.snakeBodyX[i] * CLScale,
                    snakeBodyY[i] * CLScale);

            i++;

        }
        g.setColor(new Color(0, 0, 255, 255));
        g.drawString("@", this.appleX * CLScale, this.appleY * CLScale);
        bs.show();
        g.dispose();
    }

    private void update() {
        
    }

    public void releaseAll() {
        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).down = false;
        }
    }

    private void processInput() {
        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).tick();
        }
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
    }
}

class Key {

    public int presses, absorbs;
    public boolean down, clicked;

    public void toggle(boolean pressed) {
        if (pressed != down) {
            down = pressed;
        }
        if (pressed) {
            presses++;
        }
    }

    public boolean press() {
        return down;
    }

    public void tick() {
        if (absorbs < presses) {
            absorbs++;
            clicked = true;
        } else {
            clicked = false;
        }
    }

}
