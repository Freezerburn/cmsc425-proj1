package main.gameimpl;

import main.texture.TextureManager;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 1:26 AM
 */
public class InvaderEnemy extends BaseEntity {
    protected static final float BASIC_MOVE_SPEED_RIGHT = 100.0f;
    protected static final float BASIC_MOVE_SPEED_LEFT = -100.0f;
    protected static final float DOWNWARD_MOVEMENT = 10.0f;
    public static final String ENT_NAME = "invader";
    public static final String INVADER_TEX = "res/invader.png";
    public static final String COLLISION_TYPE = "enemy";

    public static final float INVADER_WIDTH = 35.0f;
    public static final float INVADER_HEIGHT = 35.0f;
    public static final String REVERSE_DIRECTION = "reverse";
    public static final String FIRE = "fire";

    public InvaderEnemy(float x, float y, boolean movingRight) {
        super(TextureManager.loadTexture(INVADER_TEX), "none", x, y, INVADER_WIDTH, INVADER_HEIGHT);
        if(movingRight) {
            this.velocity.x = BASIC_MOVE_SPEED_RIGHT;
        }
        else {
            this.velocity.x = BASIC_MOVE_SPEED_LEFT;
        }
    }

    private void fire() {
        new Projectile(position.x + mTextures[cur].getWidth() * scale.x / 2.0f - Projectile.PROJECTILE_WIDTH / 2.0f,
                position.y - (mTextures[cur].getHeight() * scale.y) + 17.0f,
                false, false);
    }

    @Override
    public void tick(float dt) {
        this.position.x += this.velocity.x * dt;
        this.position.y += this.velocity.y * dt;
    }

    @Override
    public void preRender(float dt) {
        glPushMatrix();
        glTranslatef(this.position.x , this.position.y, 0.0f);
        glScalef(mTextures[cur].getWidth() * this.scale.x, mTextures[cur].getHeight() * this.scale.y, 0.0f);
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
        if(message.equals(REVERSE_DIRECTION)) {
            this.position.x -= this.velocity.x * 0.07;
            this.velocity.x = -this.velocity.x;
            this.position.y -= DOWNWARD_MOVEMENT;
        }
        else if(message.equals(FIRE)) {
            this.fire();
        }
        else if(parsed[0].equals("collision")) {
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
                new Vector2(this.position.x + this.mTextures[cur].getWidth() * this.scale.x,
                        this.position.y - this.mTextures[cur].getHeight() * this.scale.y));
    }

    @Override
    public String getEntName() {
        return ENT_NAME;
    }

    @Override
    public String getCollisionType() {
        return COLLISION_TYPE;
    }

    @Override
    protected void onContextEnter() {
    }

    @Override
    protected void onContextLeave() {
    }
}
