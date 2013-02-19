package main.entity;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/18/13
 * Time: 10:19 PM
 */
public abstract class EntityDecorator {
    protected EntityDecorator decorated;

    protected EntityDecorator(EntityDecorator decorated) {
        this.decorated = decorated;
    }

    public final void tick(float dt) {
        onTick(dt);
        if(decorated != null) {
            decorated.tick(dt);
        }
    }
    protected abstract void onTick(float dt);

    public final void render(float dt) {
        preRender(dt);
        onRender(dt);
        postRender(dt);
        if(decorated != null) {
            decorated.render(dt);
        }
    }
    protected abstract void onRender(float dt);
    protected abstract void preRender(float dt);
    protected abstract void postRender(float dt);
}
