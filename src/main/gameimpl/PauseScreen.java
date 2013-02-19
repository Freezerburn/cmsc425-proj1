package main.gameimpl;

import main.Game;
import main.texture.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/16/13
 * Time: 12:57 AM
 */
public class PauseScreen extends BaseEntity {
    public static final String ENT_NAME = "pause";

    protected TextEntity text;
    public boolean canDraw = false;

    protected PauseScreen() {
        super(TextureManager.DUMMY, "none", 0, 0, Game.windowWidth, Game.windowHeight);
        BufferedImage actualIm = new BufferedImage(Game.windowWidth, Game.windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = actualIm.createGraphics();
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        g.fillRect(0, 0, Game.windowWidth, Game.windowHeight);
        g.dispose();
        mTextures[0] = TextureManager.fromBufferedImage(actualIm, "pause");
        text = new TextEntity("Paused", 0, 0, 30);
        text.position.x = Game.windowWidth / 2.0f - text.getWidth() / 2.0f;
        text.position.y = Game.windowHeight / 2.0f + text.getHeight() / 2.0f;
        GameEntity.removeEntity(text);
    }

    @Override
    public void postRender(float dt) {
        if(canDraw) {
            super.postRender(dt);
            text.preRender(dt);
            text.render(dt);
            text.postRender(dt);
        }
    }
    @Override
    public void render(float dt) {
        if(canDraw) {
            super.render(dt);
        }
    }

    @Override
    public void tick(float dt) {
    }

    @Override
    protected void onDestroy() {
        text.destroy();
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
}
