package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.GameEntity;
import main.texture.Texture;
import org.lwjgl.input.Keyboard;
import stuff.Preferences;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 2:20 AM
 */
public class Player extends GameEntity implements KeyboardHandler {
    public static int moveLeftKey, moveRightKey, fireKey;
    public static final float MOVE_RIGHT_VEL = 100.0f;
    public static final float MOVE_LEFT_VEL = -100.0f;
    public static final String ENT_NAME = "player";

    protected Vector2 position, scale, velocity;
    protected boolean destroyed;

    public Player(Texture tex, float x, float y) {
        super(tex);
        this.position = new Vector2(x, y);
        this.scale = new Vector2(1.5f, 1.5f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.destroyed = false;
        moveLeftKey = Preferences.getInt("player.left", Keyboard.KEY_LEFT);
        moveRightKey = Preferences.getInt("player.right", Keyboard.KEY_RIGHT);
        fireKey = Preferences.getInt("player.fire", Keyboard.KEY_SPACE);
        InputHandler.getInstance().subscribe(this);
    }

    protected void fire() {
    }

    @Override
    public void tick(float dt) {
    }

    @Override
    public void preRender(float dt) {
        this.mTexture.bind(dt);
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glTranslatef(this.position.x , this.position.y, 0.0f);
        glScalef(mTexture.getWidth() * this.scale.x, mTexture.getHeight() * this.scale.y, 0.0f);
    }

    @Override
    public void postRender(float dt) {
        glPopMatrix();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    protected void onDestroy() {
        InputHandler.getInstance().unsubscribe(this);
    }

    @Override
    protected void handleMessage(String message) {
        String[] parsed = message.split(":");
        if(parsed[0].equals("collision")) {
            if(parsed[1].equals(Projectile.ENT_NAME)) {
                if(parsed[2].equals("no")) {
                    this.destroy();
                }
            }
            else {
                this.destroy();
            }
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

    @Override
    public void handle(InputEvent event) {
        final int keyCode = event.getKeyCode();
        if(keyCode == fireKey) {
            fire();
            event.consume();
        }
        if(keyCode == moveLeftKey) {
            if(event.isPressed()) {
                velocity.x += MOVE_LEFT_VEL;
            }
            else {
                velocity.x += MOVE_RIGHT_VEL;
            }
            event.consume();
        }
        else if(keyCode == moveRightKey) {
            if(event.isPressed()) {
                velocity.x += MOVE_RIGHT_VEL;
            }
            else {
                velocity.x += MOVE_LEFT_VEL;
            }
            event.consume();
        }
    }
}
