package main.texture;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/18/13
 * Time: 1:54 PM
 */
public class TextTexture extends Texture {
    protected int refCount = 1;
    protected ArrayList<Texture> characters;

    public TextTexture(int numChars) {
        characters = new ArrayList<Texture>(numChars);
    }

    public void addCharacter(Texture tex) {
        characters.add(tex);
    }

    @Override
    public void alloc() {
        refCount++;
    }

    @Override
    public void destroy() {
        refCount--;
        if(refCount <= 0) {
            // TODO
        }
    }

    @Override
    public float getTexBottomRightX() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexTopRightX() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexTopLeftX() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexBottomLeftX() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexBottomRightY() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexTopRightY() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexTopLeftY() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getTexBottomLeftY() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getWidth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getHeight() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getName() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getTarget() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getManagerHandle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setWidth(double width) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setHeight(double height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void bind(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValidTexture() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void restart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stopAt(int frame) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stopAfterFullCycle() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int numFrames() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
