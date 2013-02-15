package handlers;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 1/24/13
 * Time: 1:12 PM
 */
public class InputHandler {
    private static InputHandler instance;
    private LinkedList<KeyboardHandler> listeners = new LinkedList<KeyboardHandler>();

    private InputHandler() {
        try {
            Keyboard.create();
            Keyboard.enableRepeatEvents(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static InputHandler getInstance() {
        if(instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    public void subscribe(KeyboardHandler handler) {
        this.listeners.push(handler);
    }

    public void unsubscribe(KeyboardHandler handler) {
        this.listeners.remove(handler);
    }

    public void update() {
        Keyboard.poll();
        while(Keyboard.next()) {
            int keyCode = Keyboard.getEventKey();
            // Ignore the "null" code
            if(keyCode == 0) return;
            boolean state = Keyboard.getEventKeyState();
            long nanotime = Keyboard.getEventNanoseconds();
            char key = Keyboard.getEventCharacter();
            String name = Keyboard.getKeyName(keyCode);
            final InputEvent event = new InputEvent(keyCode,
                    state,
                    nanotime,
                    key,
                    name);
            for(KeyboardHandler handler : listeners) {
                handler.handle(event);
                if(event.isConsumed()) {
                    break;
                }
            }
        }
    }
}
