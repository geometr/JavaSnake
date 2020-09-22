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
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Kurochkin Konstantin <geometr.sinc@gmail.com>
 */
public class Client extends Canvas implements Runnable {

    private static final int CLIENT_WIDTH = 320;
    private static final int CLIENT_HEIGHT = 200;
    private static final int CLIENT_SCALE = 1;
    private static final int TARGET_FPS = 60;
    private static final int SQUARE_SIZE = 10;
    private static final Color BLACK = new Color(0,0,0,255);
    private static final Color WHITE = new Color(255,255,255,255);
    
    private static int CLScale = CLIENT_SCALE;
    private static int CLWidth = CLIENT_WIDTH * CLIENT_SCALE;
    private static int CLHeight = CLIENT_HEIGHT * CLIENT_SCALE;
    private BufferStrategy bs;
    
    private final Snake snake;
    private final Mouse mouse;
    private final Apple apple;

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
        snake = new Snake(new KeysInput(this), SQUARE_SIZE, CLIENT_WIDTH, CLIENT_HEIGHT);
        apple = new Apple(10, CLIENT_WIDTH, CLIENT_HEIGHT);
        mouse = new Mouse(snake, apple, SQUARE_SIZE, CLIENT_WIDTH, CLIENT_HEIGHT);

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
                update(eticks);
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
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
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

        g.setColor(BLACK);
        g.fillRect(0, 0, CLWidth, CLHeight);
        Font font = new Font("TimesRoman", Font.PLAIN, SQUARE_SIZE * CLScale);
        g.setFont(font);
        apple.render(g, CLScale);
        mouse.render(g, CLScale);
        snake.render(g, CLScale);

        g.setColor(WHITE);

        g.drawString("FPS " + FPS + " EPS " + EPS + " SCORE " + snake.len, SQUARE_SIZE * CLScale, SQUARE_SIZE * CLScale);

        bs.show();
        g.dispose();
    }

    private void update(int ticks) {
        snake.tick(ticks);
        if (snake.checkHeadCollision(mouse.x, mouse.y)) {
            snake.growAndEat();
            mouse.generate();
        }
        mouse.tick(ticks);
        if (mouse.checkBodyCollision(apple.x, apple.y)) {
            mouse.eat();
            apple.generate();
        }
        apple.tick(ticks);
    }

    private void processInput() {
        snake.input.down.tick();
        snake.input.up.tick();
        snake.input.left.tick();
        snake.input.right.tick();
    }
}
