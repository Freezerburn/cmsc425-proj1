package main;

import main.gameimpl.InvaderEnemy;
import main.gameimpl.Player;
import main.gameimpl.Projectile;
import main.texture.Texture;
import stuff.Rect2;
import stuff.TwoTuple;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/9/13
 * Time: 12:02 PM
 */
public abstract class GameEntity {
    protected static long nextUid = 0;
    public static final String FIRST_CONTEXT = "start";
    public static final String COLLISION_NONE = "none";
    protected static String currentContext = FIRST_CONTEXT;
    protected static LinkedList<GameEntity> allEntities = new LinkedList<GameEntity>();
    protected static HashMap<Long, LinkedList<GameEntity>> layers = new HashMap<Long, LinkedList<GameEntity>>();
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
        for(GameEntity ge : allEntities) {
            ge.tick(dt);
        }
    }

    /**
     * The hacky as all heck collision detection. This is almost a brute force solution, but culling
     * out certain possibilities in order to keep it from being N^2... sort of. Only attempts collision
     * on things that certain GameEntities can actually collide with. e.g. a projectile that is friendly
     * to the player will not try to collide with the player, or a projectile that is hostile will not
     * try to collide with the invaders, etc. Works well enough on my computer, but I do have an i7,
     * so your mileage mary vary.<br/>
     *
     * The only collision detection this does entirely relies upon intersecting rectangles. Once something
     * has been found to collide with something else, a message is sent to GameEntity notifying it of
     * the collision, and it can act appropriately from there. (e.g. an invader dying, your ship losing
     * health, etc.) <br/>
     *
     * Unfortunately in my haste to put together a collision detection scheme, I went a route that isn't
     * really extensible and relies on nested ifs. I'll create a better scheme for another project, or
     * later on for this one given enough time. (I'll eventually get a QuadTree/OcTree put together for
     * actually fast detection)
     */
    public static void collideAll() {
        for(GameEntity ge : allEntities) {
//            System.out.println(ge.getEntName() + ", " + Projectile.ENT_NAME);
            if(ge.getEntName().startsWith(Projectile.ENT_NAME)) {
//                System.out.println("found projetile");
                Rect2 projectileBounds = ge.getBounds();
                String[] projectileParsed = ge.getEntName().split(":");
                for(GameEntity other_ge : allEntities) {
                    if(other_ge.getEntName().startsWith(Player.ENT_NAME)) {
                        if(projectileParsed[1].equals(Projectile.HOSTILE)) {
                            Rect2 playerBounds = other_ge.getBounds();
                            boolean colliding = projectileBounds.collidesWith(playerBounds);
//                            System.out.println("Found hostile bullet. Colliding: " + colliding);
                            if(colliding) {
                                ge.handleMessage("collision:" + other_ge.getEntName());
                                other_ge.handleMessage("collision:" + ge.getEntName());
                            }
                        }
                    }
                    else if(other_ge.getEntName().startsWith(InvaderEnemy.ENT_NAME)) {
                        if(projectileParsed[1].equals(Projectile.FRIENDLY)) {
                            Rect2 invaderBounds = other_ge.getBounds();
                            boolean colliding = projectileBounds.collidesWith(invaderBounds);
//                            System.out.println("Found friendly bullet. Colliding: " + colliding);
                            if(colliding) {
                                other_ge.handleMessage("collision:" + ge.getEntName());
                                ge.handleMessage("collision:" + other_ge.getEntName());
                            }
                        }
                    }
                }
            }
            else if(ge.getEntName().startsWith(InvaderEnemy.ENT_NAME)) {

            }
        }
    }

    public static void renderAll(float dt) {
        // Theoretically should be faster due to fewer cache misses. At least according to a
        // StackOverflow thread I once read:
        // http://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-an-unsorted-array
        //
        // Also the adds/removes are done here because they are all going to happen in the tick phase
        // of entity processing, and we want to make sure there are no "ghosts" rendered of entities that
        // have actually been removed but aren't in our tracked lists yet.
        Collections.sort(addRemoveLater, twoTupleComparator);
        for(TwoTuple<Boolean, GameEntity> tt : addRemoveLater) {
            if(tt.one) {
                allEntities.remove(tt.two);
                layers.get(tt.two.getLayer()).remove(tt.two);
            }
            else {
                allEntities.add(tt.two);
                if(!layers.containsKey(tt.two.getLayer())) {
                    layers.put(tt.two.getLayer(), new LinkedList<GameEntity>());
                }
                layers.get(tt.two.getLayer()).push(tt.two);
            }
        }
        addRemoveLater.clear();

        Set<Long> keySet = layers.keySet();
        Long[] layerKeys = new Long[keySet.size()];
        keySet.toArray(layerKeys);

        Arrays.sort(layerKeys);
        for(Long layer : layerKeys) {
            for(GameEntity ge : layers.get(layer)) {
                ge.preRender(dt);
                ge.render(dt);
                ge.postRender(dt);
            }
        }
    }

    public static void destroyAll() {
        for(GameEntity ge : allEntities) {
            ge.destroy();
        }
        allEntities.clear();
        layers.clear();
        addRemoveLater.clear();
    }

    public static void switchContext(String context) {
    }

    /* ==== END OF STATIC STUFF ==== */

    private long uid = nextUid++;
    protected long layer;
    protected Texture mTexture;
    protected String context;

    public GameEntity(Texture tex, String context) {
        this.mTexture = tex;
        this.layer = 0;
        this.context = context;
        GameEntity.addEntity(this);
        if(context.equals(currentContext)) {
            onContextEnter();
        }
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
        mTexture.destroy();
        GameEntity.destroyEntity(this);
        onDestroy();
    }

    protected abstract void onDestroy();

    protected abstract void handleMessage(String message);
    public abstract boolean isDestroyed();
    public abstract Rect2 getBounds();
    public abstract String getEntName();
    public abstract String getCollisionType();
    protected abstract void onContextEnter();
    protected abstract void onContextLeave();

    public long getLayer() {
        return layer;
    }

    public long getUid() {
        return this.uid;
    }

    public String getContext() {
        return context;
    }
}
