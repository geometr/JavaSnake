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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private static final int CLIENT_SCALE = 3;
    private static final int TARGET_FPS = 60;
    private static final int SQUARE_SIZE = 10;

    // @TODO: Добавить Загрузку и сохранение
    private static enum GameStatus {
        MAINMENU, PAUSED, GAMECYCLE, GAMEOVER, HALLFAME
    }

    private static enum MainMenu {
        STARTGAME, LOADGAME, EXIT, OPTIONS, HALLFAME
    }
    private int menuCursor = 0;

    private static enum PauseMenu {
        SAVEGAME, LOADGAME, CONTINUE, EXIT, OPTIONS
    }
    private GameStatus gameStatus = GameStatus.MAINMENU;
    private static int CLScale = CLIENT_SCALE;
    private static int CLWidth = CLIENT_WIDTH * CLIENT_SCALE;
    private static int CLHeight = CLIENT_HEIGHT * CLIENT_SCALE;
    private BufferStrategy bs;
    public KeysInput input;

    private Snake snake;
    private Mouse mouse;
    private Apple apple;

    private Score[] halloffame = new Score[7];

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

        input = new KeysInput(this);

      
    }
    // TODO Auto-generated catch block

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
            if (input.escape.clicked) {
                switch (gameStatus) {
                    case GAMECYCLE:
                        gameStatus = GameStatus.PAUSED;
                        break;
                    case PAUSED:
                        gameStatus = GameStatus.GAMECYCLE;
                        break;
                    case MAINMENU:
                        break;

                }
            }
            if (gameStatus == GameStatus.HALLFAME) {
                if (input.escape.clicked) {
                    gameStatus = GameStatus.MAINMENU;
                }
            }
            if (gameStatus == GameStatus.MAINMENU) {
                if (input.down.clicked) {
                    menuCursor++;
                    if (menuCursor > 4) {
                        menuCursor = 4;

                    }
                }
                if (input.up.clicked) {
                    menuCursor--;
                    if (menuCursor < 0) {
                        menuCursor = 0;

                    }
                }
                if (input.enter.clicked) {
                    if (menuCursor == 0) {
                        if (null != snake) {
                            snake = null;
                        }
                        if (null != apple) {
                            apple = null;
                        }
                        if (null != mouse) {
                            mouse = null;
                        }
                        snake = new Snake(input, SQUARE_SIZE, CLIENT_WIDTH, CLIENT_HEIGHT);
                        apple = new Apple(10, CLIENT_WIDTH, CLIENT_HEIGHT);
                        mouse = new Mouse(SQUARE_SIZE, CLIENT_WIDTH, CLIENT_HEIGHT);
                        mouse.addApple(apple);
                        mouse.addSnake(snake);

                        gameStatus = GameStatus.GAMECYCLE;
                    }
                    if (menuCursor == 3) {
                        gameStatus = GameStatus.HALLFAME;
                    }
                    if (menuCursor == 4) {
                        break;
                    }
                }
            }
            if (gameStatus == GameStatus.GAMEOVER) {
                if (input.enter.clicked) {
                    gameStatus = GameStatus.MAINMENU;
                }
            }
            while (lag >= millisPerUpdate) {
                lag -= millisPerUpdate;
                if (gameStatus != GameStatus.PAUSED) {
                    update(eticks);
                }
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
        System.exit(0);
    }

    private void start() {
          try {
            halloffame[0] = new Score(100, "SINCLAIR");
            halloffame[1] = new Score(90, "PETER");
            halloffame[2] = new Score(80, "MARK");
            halloffame[3] = new Score(70, "ANASTASIA");
            halloffame[4] = new Score(60, "ALARA");
            halloffame[5] = new Score(50, "DASHA");
            halloffame[6] = new Score(40, "BENDER");

            File file = new File("HallOfFame.txt");
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(halloffame);
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
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

        g.setColor(Colors.BLACK);
        g.fillRect(0, 0, CLWidth, CLHeight);
        Font font = new Font("TimesRoman", Font.PLAIN, SQUARE_SIZE * CLScale);
        g.setFont(font);
        switch (this.gameStatus) {
            case GAMECYCLE:
                apple.render(g, CLScale);
                mouse.render(g, CLScale);
                snake.render(g, CLScale);
                g.setColor(Colors.WHITE);
                g.drawString("SCORE: " + snake.len, SQUARE_SIZE * CLScale * 15, SQUARE_SIZE * CLScale * 1);

                break;
            case HALLFAME:
                g.setColor(Colors.BLUE);
                g.drawString("* HALL OF FAME *", SQUARE_SIZE * CLScale * 11, SQUARE_SIZE * CLScale * 3);
                g.setColor(Colors.GREEN);
                g.drawString("1. SINCLAIR.....100", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 5);
                g.drawString("2. PETER.........90", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 7);
                g.drawString("3. MARK..........80", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 9);
                g.setColor(Colors.YELLOW);
                g.drawString("4. ANASTASIA.....70", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 11);
                g.drawString("5. ALARA.........60", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 13);
                g.drawString("6. DASHA.........50", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 15);
                g.setColor(Colors.RED);
                g.drawString("7. VERONIKA......40", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * 17);
                break;

            case MAINMENU:
                g.setColor(Colors.GREEN);
                g.drawString("S N A K E", SQUARE_SIZE * CLScale * 11, SQUARE_SIZE * CLScale * 3);
                g.setColor(Colors.WHITE);
                g.drawString("START GAME", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 6);
                g.drawString("LOAD GAME", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 8);
                g.drawString("OPTIONS", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 10);
                g.drawString("HALL OF FAME", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 12);
                g.drawString("EXIT", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 14);

                g.setColor(Colors.YELLOW);
                g.drawString("*", SQUARE_SIZE * CLScale * 9, SQUARE_SIZE * CLScale * (menuCursor * 2 + 6));
                g.drawString("*", SQUARE_SIZE * CLScale * 18, SQUARE_SIZE * CLScale * (menuCursor * 2 + 6));
                break;
            case GAMEOVER:
                g.setColor(Colors.WHITE);
                g.drawString("GAME OVER", SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 10);
                g.drawString("TOTAL SCORE: " + snake.len, SQUARE_SIZE * CLScale * 10, SQUARE_SIZE * CLScale * 12);
                break;
            case PAUSED:
                apple.render(g, CLScale);
                mouse.render(g, CLScale);
                snake.render(g, CLScale);
                g.setColor(Colors.WHITE);
                g.drawString("SCORE: " + snake.len, SQUARE_SIZE * CLScale * 15, SQUARE_SIZE * CLScale * 1);
                g.drawString("P A U S E D", SQUARE_SIZE * CLScale * 12, SQUARE_SIZE * CLScale * 10);
        }

        g.setColor(Colors.WHITE);

        g.drawString("FPS " + FPS + " EPS " + EPS, SQUARE_SIZE * CLScale, SQUARE_SIZE * CLScale);

        bs.show();
        g.dispose();
    }

    private void update(int ticks) {
        switch (this.gameStatus) {
            case GAMECYCLE:
                snake.tick(ticks);
                if (snake.checkHeadCollision(mouse.x, mouse.y)) {
                    snake.growAndEat();
                    mouse.generate();
                }
                if (snake.stamina == 0) {
                    gameStatus = GameStatus.GAMEOVER;
                    break;
                }
                mouse.tick(ticks);
                if (mouse.checkBodyCollision(apple.x, apple.y)) {
                    mouse.eat();
                    apple.generate();
                }
                apple.tick(ticks);
                break;
        }
    }

    private void processInput() {
        input.down.tick();
        input.up.tick();
        input.left.tick();
        input.right.tick();
        input.escape.tick();
        input.enter.tick();
    }
}
