package main.gameimpl;

import main.GameEntity;
import main.texture.Texture;
import stuff.Rect2;
import stuff.Vector2;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 2:21 AM
 */
public class Projectile extends GameEntity {
    public static final String ENT_NAME = "projectile";

    protected boolean friendly;
    protected String entName;

    protected Vector2 position, velocity, scale;

    public Projectile(Texture tex, float x, float y, float vel_x, float vel_y, boolean friendly) {
        super(tex);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(vel_x, vel_y);
        this.friendly = friendly;
        this.entName = ENT_NAME + ":" +  (friendly ? "yes" : "no");
    }

    @Override
    public void tick(float dt) {
        this.position.x += this.velocity.x * dt;
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
    }

    @Override
    protected void handleMessage(String message) {
        String[] parsed = message.split(":");
    }

    @Override
    public boolean isDestroyed() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Rect2 getBounds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEntName() {
        return entName;
    }
}
