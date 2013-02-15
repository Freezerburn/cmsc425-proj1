package main;

import main.texture.Texture;
import stuff.Rect2;
import stuff.TwoTuple;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/9/13
 * Time: 12:02 PM
 */
public abstract class GameEntity {
    protected static long nextUid = 0;
    protected static LinkedList<GameEntity> allEntities = new LinkedList<GameEntity>();
    protected static LinkedList<TwoTuple<Boolean, GameEntity>> addRemoveLater = new LinkedList<TwoTuple<Boolean, GameEntity>>();
    private static Comparator<TwoTuple<Boolean,GameEntity>> twoTupleComparator =
            new Comparator<TwoTuple<Boolean, GameEntity>>() {
                @Override
                public int compare(TwoTuple<Boolean, GameEntity> tt1, TwoTuple<Boolean, GameEntity> tt2) {
                    if (!tt1.one && tt2.one) {
                        return 1;
                    } else if (tt1.one && !tt2.one) {
                        return -1;
                    }
                    return 0;
                }
            };

    private static void destroyEntity(GameEntity ge) {
        addRemoveLater.push(new TwoTuple<Boolean, GameEntity>(true, ge));
    }

    private static void addEntity(GameEntity ge) {
        addRemoveLater.push(new TwoTuple<Boolean, GameEntity>(false, ge));
    }

    public static void tickAll(float dt) {
        // Theoretically should be faster due to fewer cache misses. At least according to a
        // StackOverflow thread I once read:
        // http://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-an-unsorted-array
        Collections.sort(addRemoveLater, twoTupleComparator);
        for(TwoTuple<Boolean, GameEntity> tt : addRemoveLater) {
            if(tt.one) {
                allEntities.remove(tt.two);
            }
            else {
                allEntities.add(tt.two);
            }
        }

        for(GameEntity ge : allEntities) {
            ge.tick(dt);
        }
    }

    public static void renderAll(float dt) {
        for(GameEntity ge : allEntities) {
            ge.render(dt);
        }
    }

    /* ==== END OF STATIC STUFF ==== */

    private long uid = nextUid++;
    protected Texture mTexture;

    public GameEntity(Texture tex) {
        this.mTexture = tex;
    }

    public abstract void tick(float dt);

    public abstract void preRender(float dt);
    public abstract void postRender(float dt);

    public void render(float dt) {
        mTexture.bind(dt);
//        glPushMatrix();
//        glScalef(mTexture.getWidth(), mTexture.getHeight(), 0.0f);
        glBegin(GL_QUADS);
            glColor3d(1.0, 1.0, 1.0);

            glTexCoord2d(mTexture.getTexTopRightX(), mTexture.getTexTopRightY()); // top right
            glVertex2f(1.0f, 0.0f); // bottom right

            glTexCoord2d(mTexture.getTexTopLeftX(), mTexture.getTexTopLeftY()); // top left
            glVertex2f(0.0f, 0.0f); // bottom left

            glTexCoord2d(mTexture.getTexBottomLeftX(), mTexture.getTexBottomLeftY()); // bottom left
            glVertex2f(0.0f, 1.0f); // top left

            glTexCoord2d(mTexture.getTexBottomRightX(), mTexture.getTexBottomRightY()); // bottom right
            glVertex2f(1.0f, 1.0f); // top right
        glEnd();
//        glPopMatrix();
    }

    public final void destroy() {
        GameEntity.destroyEntity(this);
        onDestroy();
    }

    protected abstract void onDestroy();

    protected abstract void handleMessage(String message);
    public abstract boolean isDestroyed();
    public abstract Rect2 getBounds();
    public abstract String getEntName();

    public long getUid() {
        return this.uid;
    }
}