package main.gameimpl;

import handlers.InputEvent;
import handlers.InputHandler;
import handlers.KeyboardHandler;
import main.Game;
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
    protected static final float END_DELAY = 1.0f;

    protected float timeUntilCanEnd;
    protected TextEntity text;

    public GameOverScreen(int status) {
        super(GAMEOVER_TEX, "none",  0.0f, 0.0f, (float)Game.windowWidth, (float)Game.windowHeight);
        this.layer = 10;
        timeUntilCanEnd = 0.0f;
        InputHandler.getInstance().subscribe(this);
    }

    @Override
    public void tick(float dt) {
        timeUntilCanEnd += dt;
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
