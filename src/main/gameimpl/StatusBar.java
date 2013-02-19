package main.gameimpl;

import main.Game;
import main.GameEntity;
import main.texture.Texture;
import main.texture.TextureManager;
import stuff.Rect2;
import stuff.Vector2;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 8:59 PM
 */
public class StatusBar extends BaseEntity {
    public static final String ENT_NAME = "status";
    public static final String STATUSBAR_TEX = "res/statusbar.png";
    public static final float BAR_HEIGHT = 24.0f;

    public static final String TAKE_DAMAGE = "player.damage";
    public static final String HEAL = "player.heal";

    protected LinkedList<GameEntity> hearts;

    public StatusBar() {
        super(STATUSBAR_TEX, "none", 0.0f, 0.0f, Game.windowWidth, BAR_HEIGHT);
        hearts = new LinkedList<GameEntity>();
        for(int i = 0; i < Player.START_HIT_POINTS; i++) {
            hearts.push(new HealthHeart(10.0f + i * (HealthHeart.HEART_WIDTH + 5.0f), 0.0f));
        }
    }

    @Override
    public void tick(float dt) {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected void handleMessage(String message) {
        if(message.equals(TAKE_DAMAGE)) {
            hearts.getFirst().destroy();
            hearts.removeFirst();
        }
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
}
