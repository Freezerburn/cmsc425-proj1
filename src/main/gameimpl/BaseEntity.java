package main.gameimpl;

import main.GameEntity;
import main.texture.Texture;
import main.texture.TextureManager;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 9:15 PM
 */
public abstract class BaseEntity extends GameEntity {
    protected Vector2 position, velocity, scale;
    protected boolean destroyed;

    protected BaseEntity(String file, String context, float x, float y, float width, float height) {
        super(TextureManager.loadTexture(file), context);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.scale = new Vector2(width / mTexture.getWidth(), height / mTexture.getHeight());
        destroyed = false;
    }

    protected BaseEntity(Texture tex, String context, float x, float y, float width, float height) {
        super(tex, context);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.scale = new Vector2(width / mTexture.getWidth(), height / mTexture.getHeight());
        destroyed = false;
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
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public Rect2 getBounds() {
        return new Rect2(this.position,
                new Vector2(this.position.x + this.mTexture.getWidth() * this.scale.x,
                        this.position.y - this.mTexture.getHeight() * this.scale.y));
    }
}
