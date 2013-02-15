package main;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 12:36 AM
 */
public interface GameRunnable {
    public void initGL(int width, int height);
    public void initData(int fps);
    public void run();
}
