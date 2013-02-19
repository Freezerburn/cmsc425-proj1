package main.entity;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/18/13
 * Time: 10:26 PM
 */
public class PositionEntity extends EntityDecorator {
    public PositionEntity() {
        super(null);
    }

    public PositionEntity(EntityDecorator decorated) {
        super(decorated);
    }

    @Override
    protected void onTick(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onRender(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void preRender(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void postRender(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
