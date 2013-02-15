package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
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
public class SpaceInvaders implements GameRunnable {
    protected static long score = 0;

    private boolean running;
    protected int fps;
    protected int currentLevel;
    protected LinkedList<InvadersLevel> levels;

    public static Player player;

    public static void addScore(long toAdd) {
        score += toAdd;
    }

    @Override
    public void initGL(int width, int height) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0, width, 0, height);
        glClearColor(1.0f, 0.3f, 0.3f, 1.0f);
    }

    @Override
    public void initData(int fps) {
        this.running = true;
        this.fps = fps;
        System.out.println("FPS: " + fps);
        this.currentLevel = 0;
        this.levels = new LinkedList<InvadersLevel>();
        this.levels.push(new InvaderLevel1());
        player = new Player(TextureManager.loadTexture("res/invader.png"), 200.0f, 200.0f);
        InputHandler.getInstance().subscribe(new KeyboardHandler() {
            @Override
            public void handle(InputEvent event) {
                if (event.getKeyCode() == Keyboard.KEY_Q || event.getKeyCode() == Keyboard.KEY_ESCAPE) {
                    running = false;
                    event.consume();
                }
            }
        });
    }

    @Override
    public void run() {
        long totalTicks = 0;
        long lastTime = System.nanoTime();
        while(this.running) {
            totalTicks++;
            InputHandler.getInstance().update();

            glClear(GL_COLOR_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            long curTime = System.nanoTime();
            float delta = (curTime - lastTime) / 10000000000.0f;
            if(totalTicks % 30 == 0) {
                System.out.println("DT: " + delta);
            }
            lastTime = curTime;
            GameEntity.tickAll(delta);
            GameEntity.renderAll(delta);

            Display.update();
            Display.sync(this.fps);
            if(Display.isCloseRequested()) {
                this.running = false;
            }
        }
    }
}
