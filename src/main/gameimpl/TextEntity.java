package main.gameimpl;

import main.texture.TextureManager;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/16/13
 * Time: 2:02 AM
 */
public class TextEntity extends BaseEntity {
    public static final String ENT_NAME = "text";

    protected String name;

    protected TextEntity(String text, float x, float y, float width, float height) {
        super(TextureManager.fromString(text), "none", x, y, width, height);
        name = ENT_NAME + ":" + text;
    }

    @Override
    public void tick(float dt) {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected void handleMessage(String message) {
    }

    @Override
    public String getEntName() {
        return name;
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
