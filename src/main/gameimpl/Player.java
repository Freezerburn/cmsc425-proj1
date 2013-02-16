package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.GameEntity;
import main.texture.Texture;
import main.texture.TextureManager;
import org.lwjgl.input.Keyboard;
import stuff.Preferences;
import stuff.Rect2;
import stuff.Vector2;

import java.util.LinkedList;
import java.util.ListIterator;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 2:20 AM
 */
public class Player extends GameEntity implements KeyboardHandler {
    public static int moveLeftKey, moveRightKey, fireKey;
    public static final float MOVE_RIGHT_VEL = 300.0f;
    public static final float MOVE_LEFT_VEL = -MOVE_RIGHT_VEL;
    public static final float PLAYER_WIDTH = 48.0f;
    public static final float PLAYER_HEIGHT = 48.0f;
    public static final String ENT_NAME = "player";

    public static final String PLAYER_TEX = "res/player.png";

    protected static final int MAX_SHOTS = 5;
    protected static final float TIME_BETWEEN_SHOTS = 0.5f;

    protected Vector2 position, scale, velocity;
    protected boolean destroyed;
    protected LinkedList<Projectile> shots;
    protected float timeSinceLastShot;
    protected Vector2 projectileStart;

    public Player(float x, float y) {
        super(TextureManager.loadTexture(PLAYER_TEX));
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.scale = new Vector2(PLAYER_WIDTH / mTexture.getWidth(), PLAYER_HEIGHT / mTexture.getHeight());
        this.destroyed = false;
        this.shots = new LinkedList<Projectile>();
        this.timeSinceLastShot = TIME_BETWEEN_SHOTS;
        this.projectileStart = new Vector2(x + PLAYER_WIDTH / 2.0f - Projectile.PROJECTILE_WIDTH / 2.0f,
                y + Projectile.PROJECTILE_HEIGHT * 2.0f + 3.0f);
        moveLeftKey = Preferences.getInt("player.left", Keyboard.KEY_LEFT);
        System.out.println("Player.moveLeftKey: " + moveLeftKey + " vs " + Keyboard.KEY_LEFT);
        moveRightKey = Preferences.getInt("player.right", Keyboard.KEY_RIGHT);
        fireKey = Preferences.getInt("player.fire", Keyboard.KEY_SPACE);
        InputHandler.getInstance().subscribe(this);
    }

    protected void fire() {
        if(timeSinceLastShot > TIME_BETWEEN_SHOTS) {
            if(shots.size() < MAX_SHOTS) {
                Projectile shot = new Projectile(this.projectileStart.x, this.projectileStart.y, true, true);
                shots.push(shot);
            }
        }
    }

    @Override
    public void tick(float dt) {
        timeSinceLastShot += dt;
        if(!shots.isEmpty()) {
            ListIterator it = shots.listIterator();
            while(it.hasNext()) {
                Projectile shot = (Projectile)it.next();
                if(shot.isDestroyed()) {
                    it.remove();
                }
            }
        }
        this.position.x += this.velocity.x * dt;
        this.projectileStart.x += this.velocity.x * dt;
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
                        this.position.y - this.mTexture.getHeight() * this.scale.y));
    }

    @Override
    public String getEntName() {
        return ENT_NAME;
    }

    @Override
    public void handle(InputEvent event) {
        final int keyCode = event.getKeyCode();
        if(keyCode == fireKey && event.isPressed()) {
            fire();
            event.consume();
        }
        if(keyCode == moveLeftKey) {
            if(event.isPressed()) {
                if(velocity.x >= 0.0) {
                    velocity.x += MOVE_LEFT_VEL;
                }
            }
            else {
                if(velocity.x <= 0.0) {
                    velocity.x += MOVE_RIGHT_VEL;
                }
            }
            event.consume();
        }
        else if(keyCode == moveRightKey) {
            if(event.isPressed()) {
                if(velocity.x <= 0.0) {
                    velocity.x += MOVE_RIGHT_VEL;
                }
            }
            else {
                if(velocity.x >= 0.0) {
                    velocity.x += MOVE_LEFT_VEL;
                }
            }
            event.consume();
        }
    }
}
