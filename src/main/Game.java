package main;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.texture.TextureManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import stuff.Preferences;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 1/24/13
 * Time: 11:29 AM
 */
public class Game {
    protected static final String WINDOW_NAME = "CMSC425 - GameDev";
    protected static final String CMDLINE_WINDOW_NAME = "cmsc425.window.name";

    protected static final int WINDOW_WIDTH = 640;
    protected static final int WINDOW_HEIGHT = 480;
    protected static final String CMDLINE_WINDOW_WIDTH = "cmsc425.window.width";
    protected static final String CMDLINE_WINDOW_HEIGHT = "cmsc425.window.height";

    protected static final int FPS = 60;
    protected static final String CMDLINE_FPS = "cmsc425.window.fps";

    protected boolean isRunning;
    protected int fps;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        this.isRunning = true;
        this.fps = Game.FPS;
        if(System.getProperties().containsKey(Game.CMDLINE_FPS)) {
            this.fps = Integer.parseInt(System.getProperty(Game.CMDLINE_FPS));
        }

        if(System.getProperties().containsKey(Preferences.CMDLINE_FILENAME)) {
            Preferences.restoreAll(System.getProperty(Preferences.CMDLINE_FILENAME));
        }
        else {
            Preferences.restoreAll();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if(System.getProperties().containsKey(Preferences.CMDLINE_FILENAME)) {
                    Preferences.saveAll(System.getProperty(Preferences.CMDLINE_FILENAME));
                }
                else {
                    Preferences.saveAll();
                }
            }
        }));

        try {
            if(System.getProperties().containsKey(Game.CMDLINE_WINDOW_NAME)) {
                Display.setTitle(System.getProperty(Game.CMDLINE_WINDOW_NAME));
            }
            else {
                Display.setTitle(Game.WINDOW_NAME);
            }

            int width = Game.WINDOW_WIDTH;
            int height = Game.WINDOW_HEIGHT;
            if(System.getProperties().containsKey(Game.CMDLINE_WINDOW_WIDTH)) {
                width = Integer.parseInt(System.getProperty(Game.CMDLINE_WINDOW_WIDTH));
            }
            if(System.getProperties().containsKey(Game.CMDLINE_WINDOW_HEIGHT)) {
                height = Integer.parseInt(System.getProperty(Game.CMDLINE_WINDOW_HEIGHT));
            }
            Display.setDisplayMode(new DisplayMode(width, height));

            Display.create();
            Display.processMessages();
            InputHandler.getInstance().subscribe(new KeyboardHandler() {
                @Override
                public void handle(InputEvent event) {
                    if(event.getKeyCode() == Keyboard.KEY_Q || event.getKeyCode() == Keyboard.KEY_ESCAPE) {
                        isRunning = false;
                        event.consume();
                    }
                }
            });
            this.initGL(width, height);
            this.run();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        finally {
            Display.destroy();
            Keyboard.destroy();
        }
        System.exit(0);
    }

    protected void initGL(int width, int height) {
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

    protected void run() {
//        GameEntity ent = new GameEntity(TextureManager.loadTexture("res/punchafish.png"));
        // Warm up nanotime
        for(int i = 0; i < 10000; i++) {
            System.nanoTime();
        }

        long lastTime = System.nanoTime();
        while(this.isRunning) {
            InputHandler.getInstance().update();

            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

//            ent.render(0.0f);
            long curTime = System.nanoTime();
            float delta = (lastTime - curTime) / 1000000000.0f;
            lastTime = curTime;
            GameEntity.tickAll(delta);
            GameEntity.renderAll(delta);

            Display.update();
            Display.sync(this.fps);
            if(Display.isCloseRequested()) {
                this.isRunning = false;
            }
        }
    }
}
