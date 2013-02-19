package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.Game;
import main.texture.Texture;
import main.texture.TextureManager;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 10:01 PM
 */
public class GameOverScreen extends BaseEntity implements KeyboardHandler {
    public static final String ENT_NAME = "gameoverman";
    public static final String GAMEOVER_TEX = "res/gameover.png";
    protected static final float END_DELAY = 0.5f;

    protected float timeUntilCanEnd;
    protected boolean quitting;
    protected TextEntity text;

    public GameOverScreen(int status) {
        super(TextureManager.DUMMY, "none",  0.0f, 0.0f, (float)Game.windowWidth, (float)Game.windowHeight);
        text = new TextEntity(screenText(status), 0, 0, 30);
        text.position.x = Game.windowWidth / 2.0f - text.getWidth() / 2.0f;
        text.position.y = Game.windowHeight / 2.0f + text.getHeight() / 2.0f;
        this.layer = 10;
        timeUntilCanEnd = 0.0f;
        InputHandler.getInstance().subscribe(this);
    }

    @Override
    public void preRender(float dt) {
        text.preRender(dt);
    }
    public void postRender(float dt) {
        text.postRender(dt);
    }
    public void render(float dt) {
        text.render(dt);
    }

    protected String screenText(int status) {
        if(status == SpaceInvaders.STATUS_LOSE) {
            return "Game Over Man: You Lost!";
        }
        else if(status == SpaceInvaders.STATUS_WON) {
            return "Congratulations: You won!";
        }
        quitting = true;
        return "Quitting game...";
    }

    @Override
    public void tick(float dt) {
        timeUntilCanEnd += dt;
        if(timeUntilCanEnd > END_DELAY) {
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        InputHandler.getInstance().unsubscribe(this);
        destroyed = true;
    }

    @Override
    protected void handleMessage(String message) {
    }

    @Override
    public String getEntName() {
        return ENT_NAME;
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

    @Override
    public void handle(InputEvent event) {
        if(timeUntilCanEnd > END_DELAY) {
            this.destroy();
        }
    }
}
