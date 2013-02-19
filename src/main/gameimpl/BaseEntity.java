package main.gameimpl;

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
        this.scale = new Vector2(width / mTextures[cur].getWidth(), height / mTextures[cur].getHeight());
        destroyed = false;
    }

    protected BaseEntity(Texture tex, String context, float x, float y, float width, float height) {
        super(tex, context);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.scale = new Vector2(width / mTextures[cur].getWidth(), height / mTextures[cur].getHeight());
        destroyed = false;
    }

    protected BaseEntity(Texture[] textures, String context, float x, float y, float width, float height) {
        super(textures, context);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        if(textures == null) {
            this.scale = new Vector2(1.0f, 1.0f);
        }
        else {
            this.scale = new Vector2(width / mTextures[cur].getWidth(), height / mTextures[cur].getHeight());
        }
        destroyed = false;
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
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public Rect2 getBounds() {
        return new Rect2(this.position,
                new Vector2(this.position.x + this.mTextures[cur].getWidth() * this.scale.x,
                        this.position.y - this.mTextures[cur].getHeight() * this.scale.y));
    }
}
