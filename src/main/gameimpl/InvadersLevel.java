package main.gameimpl;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 1:23 AM
 */
public interface InvadersLevel {
    public void initInvaders();
    public boolean levelFinished();
    public void tick(float dt);
}
