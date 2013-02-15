package handlers;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 1/24/13
 * Time: 1:13 PM
 */
public class InputEvent {
    protected int keyCode;
    protected boolean state;
    protected long nanoTime;
    protected char key;
    protected String name;
    protected boolean consumed;

    protected InputEvent(int keyCode, boolean state, long nanoTime, char key, String name) {
        this.keyCode = keyCode;
        this.state = state;
        this.nanoTime = nanoTime;
        this.key = key;
        this.name = name;
        this.consumed = false;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public boolean isPressed() {
        return this.state;
    }

    public long getTime() {
        return this.nanoTime;
    }

    public long getTimeInMillis() {
        return this.nanoTime / 1000000;
    }

    public float getTimeInSeconds() {
        return this.nanoTime / 1000000000.0f;
    }

    public char getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public void consume() {
        this.consumed = true;
    }

    public boolean isConsumed() {
        return this.consumed;
    }
}
