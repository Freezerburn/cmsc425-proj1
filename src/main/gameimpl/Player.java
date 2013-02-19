package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.Game;
import main.texture.TextureManager;
import org.lwjgl.input.Keyboard;
import stuff.Preferences;
import stuff.Rect2;
import stuff.Vector2;

import java.util.LinkedList;
import java.util.ListIterator;

import static org.lwjgl.opengl.GL11.*;

/**
 * The GameEntity for the player. A laser cannon that can shoot a certain number of bullets at a certain
 * rate, and has a certain amount of health before getting a game over.
 * Author: Vincent K.
 * Date: 2/15/13
 * Time: 2:20 AM
 */
public class Player extends BaseEntity implements KeyboardHandler {
    // The key codes that tell the player to move left/right or fire. By default, these are assigned to
    // Display.KEY_LEFT/RIGHT/SPACE.
    public static int moveLeftKey, moveRightKey, fireKey;
    // The speed (in pixels/second) the cannon moves right/left.
    public static final float MOVE_RIGHT_VEL = 300.0f;
    public static final float MOVE_LEFT_VEL = -MOVE_RIGHT_VEL;
    // The size of the player on-screen. The actual sprite will be auto-scaled to this.
    public static final float PLAYER_WIDTH = 48.0f;
    public static final float PLAYER_HEIGHT = 37.0f;
    // The name of the player, used for stuff such as collision. Might be able to use instanceof,
    // but I'm leaving it as a String in case it becomes important for message passing.
    public static final String ENT_NAME = "player";
    // The collision group the player belongs to. Only things that belong to groups specifically registered
    // to collide with this group will actually collide.
    public static final String COLLISION_TYPE = "player";
    // The number of hits the player can take before getting a game over (at the start of the game).
    public static final int START_HIT_POINTS = 3;

    // Location of the player sprite.
    public static final String PLAYER_TEX = "res/player.png";

    // The player can only have a certain number of shots on-screen, and can only fire at a specific
    // rate which these two constants describe.
    protected static final int MAX_SHOTS = 5;
    protected static final float TIME_BETWEEN_SHOTS = 0.5f;

    // Tracks the shots a player has fired, so that we can know when to disallow shooting, or allow
    // shooting again.
    protected LinkedList<Projectile> shots;
    // Tracks the time it's been since the player fired a shot.
    protected float timeSinceLastShot;
    // The location to create a projectile at. Has to be updated when the player moves.
    protected Vector2 projectileStart;
    // Our current health, so we know when to die.
    protected int health;

    protected boolean firstPress = false;

    public Player(String context, float x, float y) {
        super(TextureManager.loadTexture(PLAYER_TEX), context, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
        this.shots = new LinkedList<Projectile>();
        this.timeSinceLastShot = TIME_BETWEEN_SHOTS;
        this.health = START_HIT_POINTS;
        this.projectileStart = new Vector2(x + PLAYER_WIDTH / 2.0f - Projectile.PROJECTILE_WIDTH / 2.0f,
                y + Projectile.PROJECTILE_HEIGHT * 2.0f + 3.0f);
        moveLeftKey = Preferences.getInt("player.left", Keyboard.KEY_LEFT);
        moveRightKey = Preferences.getInt("player.right", Keyboard.KEY_RIGHT);
        fireKey = Preferences.getInt("player.fire", Keyboard.KEY_SPACE);
        InputHandler.getInstance().subscribe(this);
    }

    protected void fire() {
        // While the Invaders themselves will not fire while the game is paused due to how their random firing
        // implementation works, the cannon can still be fired unless we specifically disallow it here.
        if(SpaceInvaders.paused) {
            return;
        }
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
        if(position.x + (mTextures[cur].getWidth() * scale.x) > Game.windowWidth) {
            float newx = Game.windowWidth - (mTextures[cur].getWidth() * scale.x);
            float diff = position.x - newx;
            projectileStart.x -= diff;
            this.position.x = newx;
        }
        else if(position.x < 0.0f) {
            projectileStart.x -= position.x;
            position.x = 0.0f;
        }
        this.projectileStart.x += this.velocity.x * dt;
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
        InputHandler.getInstance().unsubscribe(this);
        destroyed = true;
    }

    @Override
    protected void handleMessage(String message) {
        String[] parsed = message.split(":");
        if(parsed[0].equals("collision")) {
            if(parsed[1].equals(Projectile.ENT_NAME)) {
                if(parsed[2].equals("no")) {
                    this.health--;
                    SpaceInvaders.statusBar.handleMessage(StatusBar.TAKE_DAMAGE);
                    if(this.health == 0) {
                        this.destroy();
                    }
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
        return new Rect2(new Vector2(this.position.x + 13.0f, this.position.y - 11.0f),
                new Vector2(this.position.x + this.mTextures[cur].getWidth() * this.scale.x - 13.0f,
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
    public String getContext() {
        return context;
    }

    @Override
    protected void onContextEnter() {
//        InputHandler.getInstance().subscribe(this);
    }

    @Override
    protected void onContextLeave() {
//        InputHandler.getInstance().unsubscribe(this);
    }

    @Override
    public void handle(InputEvent event) {
        // Ignore the first event given to us. This is to avoid the possibility of a player hitting
        // left/right when starting the game, and causing the ship to start moving in one direction
        // of its own volition until the player presses the key for that direction.
        if(!firstPress) {
            firstPress = true;
            return;
        }
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
