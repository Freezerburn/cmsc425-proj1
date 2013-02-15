package main;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.gameimpl.SpaceInvaders;
import main.texture.TextureManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.OpenGLException;
import stuff.Preferences;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 1/24/13
 * Time: 11:29 AM
 */
public class Game implements GameRunnable {
    protected static final String WINDOW_NAME = "CMSC425 - GameDev";
    protected static final String CMDLINE_WINDOW_NAME = "cmsc425.window.name";

    protected static final int WINDOW_WIDTH = 640;
    protected static final int WINDOW_HEIGHT = 480;
    protected static final String CMDLINE_WINDOW_WIDTH = "cmsc425.window.width";
    protected static final String CMDLINE_WINDOW_HEIGHT = "cmsc425.window.height";

    protected static final int FPS = 60;
    protected static final String CMDLINE_FPS = "cmsc425.window.fps";

    public static int windowWidth;
    public static int windowHeight;
    public static int fps;

    public static void main(String[] args) {
        try {
            initBase();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Warm up the nanoTime function
        for(int i = 0; i < 10000; i++) {
            System.nanoTime();
        }

        try {
            GameRunnable game = new SpaceInvaders();
            game.initGL(windowWidth, windowHeight);
            game.initData(fps);
            game.run();
        }
        catch (OpenGLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            Display.destroy();
            if(Keyboard.isCreated()) {
                Keyboard.destroy();
            }
            System.exit(2);
        }
        System.exit(0);
    }

    public static void initBase() throws LWJGLException {
        if(System.getProperties().containsKey(Game.CMDLINE_FPS)) {
            fps = Integer.parseInt(System.getProperty(Game.CMDLINE_FPS));
        }
        else {
            fps = FPS;
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
                if (System.getProperties().containsKey(Preferences.CMDLINE_FILENAME)) {
                    Preferences.saveAll(System.getProperty(Preferences.CMDLINE_FILENAME));
                } else {
                    Preferences.saveAll();
                }
            }
        }));

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
        windowWidth = width;
        windowHeight = height;
    }

    @Override
    public void initGL(int width, int height) {
    }

    @Override
    public void initData(int fps) {
    }

    @Override
    public void run() {
    }
}
