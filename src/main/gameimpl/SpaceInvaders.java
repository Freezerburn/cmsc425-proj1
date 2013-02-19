package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.Game;
import main.GameEntity;
import main.GameRunnable;
import main.texture.TextureManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.Arrays;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 12:35 AM
 */
public class SpaceInvaders implements GameRunnable, KeyboardHandler {
    protected static long score = 0;
    public static final int STATUS_WON = 0;
    public static final int STATUS_LOSE = 1;
    public static final int STATUS_QUIT = 2;

    public static final float BASE_WIDTH = 640.0f;
    public static final float BASE_HEIGHT = 480.0f;

    private boolean running;
    protected int fps;
    protected int currentLevel;
    protected LinkedList<InvadersLevel> levels;
    protected boolean gameEnded;
    protected GameOverScreen gameOver;

    public static Player player;
    public static StatusBar statusBar;
    public static boolean paused;

    public static void addScore(long toAdd) {
        score += toAdd;
    }

    @Override
    public void initGL(int width, int height) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glShadeModel(GL_SMOOTH);
//        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0, width, 0, height);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void initData(int fps) {
        this.running = true;
        this.fps = fps;
        System.out.println("FPS: " + fps);
        this.currentLevel = 0;
        this.levels = new LinkedList<InvadersLevel>();
        this.levels.push(new InvaderLevel1());
        this.gameEnded = false;
        paused = false;
        player = new Player(GameEntity.FIRST_CONTEXT,
                Game.windowWidth / 2.0f + Player.PLAYER_WIDTH / 2.0f,
                StatusBar.BAR_HEIGHT);
        statusBar = new StatusBar();
        InputHandler.getInstance().subscribe(this);
        this.levels.get(currentLevel).initInvaders();
        Display.setResizable(true);
    }

    @Override
    public void run() {
        long totalTicks = 0;
        float scale_x = (float)Game.windowWidth / BASE_WIDTH;
        float scale_y = (float)Game.windowHeight / BASE_HEIGHT;
        long lastTime = System.nanoTime();
        while(this.running) {
            totalTicks++;
            if(!gameEnded) {
                if(levels.get(currentLevel).levelFinished()) {
                    currentLevel++;
                    if(currentLevel == levels.size()) {
                        endGame(STATUS_WON);
                    }
                    else {
                        levels.get(currentLevel).initInvaders();
                    }
                }
                else if(player.isDestroyed()) {
                    endGame(STATUS_LOSE);
                }
            }
            else if(gameOver.isDestroyed()) {
                running = false;
            }
            if(Display.wasResized()) {
                Game.windowWidth = Display.getWidth();
                Game.windowHeight = Display.getHeight();
                scale_x = (float)Game.windowWidth / BASE_WIDTH;
                scale_y = (float)Game.windowHeight / BASE_HEIGHT;
                System.out.println("resize: " + scale_x + ", " + scale_y);
                glViewport(0, 0, Game.windowWidth, Game.windowHeight);
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                gluOrtho2D(0, Game.windowWidth, 0, Game.windowHeight);
            }
            InputHandler.getInstance().update();

            glClear(GL_COLOR_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glScalef(scale_x, scale_y, 1.0f);

            long curTime = System.nanoTime();
            float delta = (curTime - lastTime) / 1000000000.0f;
            if(paused) {
                delta = 0.0f;
            }
//            if(totalTicks % 30 == 0) {
//                System.out.println("DT: " + delta);
//            }
            lastTime = curTime;
            GameEntity.tickAll(delta);
            if(!gameEnded) {
                levels.get(currentLevel).tick(delta);
            }
            GameEntity.collideAll();
            GameEntity.renderAll(delta);

            Display.update();
            Display.sync(this.fps);
            if(Display.isCloseRequested()) {
                this.running = false;
            }
        }
    }

    protected void restart() {
        GameEntity.destroyAll();
        InputHandler.getInstance().unsubscribe(this);
        initData(fps);
    }

    protected void endGame(int status) {
        if(gameEnded) {
            this.running = false;
        }
        else {
            gameEnded = true;
            if(status == STATUS_WON) {
                System.out.println("You win!");
            }
            else if(status == STATUS_LOSE) {
                System.out.println("You lose!");
            }
            else if(status == STATUS_QUIT) {
                System.out.println("Quit game");
            }
            GameEntity.destroyAll();
            gameOver = new GameOverScreen(status);
        }
    }

    @Override
    public void handle(InputEvent event) {
        if (event.getKeyCode() == Keyboard.KEY_Q || event.getKeyCode() == Keyboard.KEY_ESCAPE) {
//                    running = false;
            endGame(STATUS_QUIT);
            event.consume();
        } else if (event.getKeyCode() == Keyboard.KEY_R && event.isPressed()) {
            restart();
        } else if (event.getKeyCode() == Keyboard.KEY_P && event.isPressed()) {
            paused = !paused;
        }
    }
}
