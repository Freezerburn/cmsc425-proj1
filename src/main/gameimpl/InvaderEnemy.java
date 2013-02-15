package main.gameimpl;

import main.GameEntity;
import main.texture.Texture;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 1:26 AM
 */
public class InvaderEnemy extends GameEntity {
    protected static final float BASIC_MOVE_SPEED_RIGHT = 100.0f;
    protected static final float BASIC_MOVE_SPEED_LEFT = -100.0f;
    public static final String ENT_NAME = "invader";

    public static final float INVADER_WIDTH = 32.0f;
    public static final float INVADER_HEIGHT = 32.0f;
    public static final String REVERSE_DIRECTION = "reverse";
    public static final String FIRE = "fire";

    protected Vector2 position, velocity, scale;
    protected boolean destroyed;

    public InvaderEnemy(Texture tex, float x, float y, boolean movingRight) {
        super(tex);
        this.position = new Vector2(x, y);
        if(movingRight) {
            this.velocity = new Vector2(BASIC_MOVE_SPEED_RIGHT, 0.0f);
        }
        else {
            this.velocity = new Vector2(BASIC_MOVE_SPEED_LEFT, 0.0f);
        }
        this.scale = new Vector2(INVADER_WIDTH / tex.getWidth(), INVADER_HEIGHT / tex.getHeight());
        this.destroyed = false;
    }

    private void fire() {
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
        if(message.equals(REVERSE_DIRECTION)) {
            this.velocity.x = -this.velocity.x;
        }
        else if(message.equals(FIRE)) {
            this.fire();
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
                        this.position.y + this.mTexture.getHeight() * this.scale.y));
    }

    @Override
    public String getEntName() {
        return ENT_NAME;
    }
}
