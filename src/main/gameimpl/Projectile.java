package main.gameimpl;

import main.Game;
import main.GameEntity;
import main.texture.Texture;
import main.texture.TextureManager;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 2:21 AM
 */
public class Projectile extends BaseEntity {
    public static final String ENT_NAME = "projectile";
    public static final String HOSTILE = "no";
    public static final String FRIENDLY = "yes";

    public static final float PROJECTILE_VEL = 300.0f;
    public static final float PROJECTILE_WIDTH = 10.0f;
    public static final float PROJECTILE_HEIGHT = 20.0f;
    public static final String PROJECTILE_TEX = "res/projectile.png";

    protected boolean friendly;
    protected String entName;

    public Projectile(float x, float y, boolean goingUp, boolean friendly) {
        super(TextureManager.loadTexture(PROJECTILE_TEX), "none", x, y, PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
        if(goingUp) {
            this.velocity.y = PROJECTILE_VEL;
        }
        else {
            this.velocity.y = -PROJECTILE_VEL;
        }
        this.friendly = friendly;
        if(!friendly) {
            this.velocity.y /= 2.0f;
        }
        this.entName = ENT_NAME + ":" +  (friendly ? FRIENDLY : HOSTILE);
    }

    @Override
    public void tick(float dt) {
        if(this.position.y + PROJECTILE_HEIGHT > Game.windowHeight) {
            this.destroy();
        }
        else if(this.position.y < 0.0f) {
            this.destroy();
        }
        this.position.x += this.velocity.x * dt;
        this.position.y += this.velocity.y * dt;
    }

    @Override
    public void preRender(float dt) {
        glPushMatrix();
        glTranslatef(this.position.x , this.position.y, 0.0f);
        glScalef(mTexture.getWidth() * this.scale.x, mTexture.getHeight() * this.scale.y, 0.0f);
    }

    @Override
    public void postRender(float dt) {
        glPopMatrix();
    }

    @Override
    protected void onDestroy() {
        this.destroyed = true;
    }

    @Override
    protected void handleMessage(String message) {
        String[] parsed = message.split(":");
        if(parsed[0].equals("collision")) {
            this.destroy();
        }
    }

    @Override
    public boolean isDestroyed() {
        return this.destroyed;
    }

    @Override
    public Rect2 getBounds() {
        return new Rect2(this.position,
                new Vector2(this.position.x + this.mTexture.getWidth() * this.scale.x,
                        this.position.y - this.mTexture.getHeight() * this.scale.y));
    }

    @Override
    public String getEntName() {
        return entName;
    }

    @Override
    public String getCollisionType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onContextEnter() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onContextLeave() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
