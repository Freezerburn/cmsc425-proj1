package main.gameimpl;

import main.texture.Texture;
import main.texture.TextureManager;
import stuff.TwoTuple;
import stuff.Vector2;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/16/13
 * Time: 2:02 AM
 */
public class TextEntity extends BaseEntity {
    public static final String ENT_NAME = "text";

    protected String name;
    protected ArrayList<TwoTuple<Vector2, Vector2>> posSizePairs;

    public TextEntity(String text, float x, float y) {
        super(TextureManager.fromString(text), "none", x, y, 18, 18);
        name = ENT_NAME + ":" + text;
    }

    public TextEntity(String text, float x, float y, int pointSize) {
        super(TextureManager.fromString(text, pointSize), "none", x, y, 18, 18);
        name = ENT_NAME + ":" + text;
    }

    protected void generatePairs() {
        float x = position.x;
        float y = position.y;
        posSizePairs = new ArrayList<TwoTuple<Vector2, Vector2>>(mTextures.length);
        float width = mTextures[0].getWidth(), height = mTextures[0].getHeight();
        posSizePairs.add(new TwoTuple<Vector2, Vector2>(new Vector2(width, height), new Vector2(x, y)));
        float curx = x + width;
        for(int i = 1; i < mTextures.length; i++) {
            width = mTextures[i].getWidth();
            height = mTextures[i].getHeight();
            posSizePairs.add(new TwoTuple<Vector2, Vector2>(new Vector2(width, height), new Vector2(curx, y)));
            curx += width;
        }
    }

    @Override
    public void preRender(float dt) {
        if(posSizePairs == null) {
            generatePairs();
        }
        glPushMatrix();
        TwoTuple<Vector2, Vector2> posSize = posSizePairs.get(cur);
        glTranslatef(posSize.two.x, posSize.two.y, 0.0f);
        glScalef(posSize.one.x, posSize.one.y, 1.0f);
    }

    @Override
    public void postRender(float dt) {
        glPopMatrix();
    }

    @Override
    public void render(float dt) {
        if(cur == mTextures.length) {
            cur = 0;
        }
        else {
            super.render(dt);
            cur++;
            if(cur != mTextures.length) {
                postRender(dt);
                preRender(dt);
            }
            render(dt);
        }
    }

    @Override
    public void tick(float dt) {
    }

    @Override
    protected void onDestroy() {
        System.out.println(name + ", being destroyed");
    }

    @Override
    protected void handleMessage(String message) {
    }

    @Override
    public String getEntName() {
        return name;
    }

    @Override
    public String getCollisionType() {
        return COLLISION_NONE;
    }

    @Override
    protected void onContextEnter() {
    }

    @Override
    protected void onContextLeave() {
    }

    public float getWidth() {
        float ret = 0.0f;
        for(Texture tex : mTextures) {
            ret += tex.getWidth();
        }
        return ret;
    }

    public float getHeight() {
        return mTextures[0].getHeight();
    }
}
