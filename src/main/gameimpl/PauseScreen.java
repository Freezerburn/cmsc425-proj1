package main.gameimpl;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/16/13
 * Time: 12:57 AM
 */
public class PauseScreen extends BaseEntity {
    public static final String ENT_NAME = "pause";

    protected PauseScreen(String file, float x, float y, float width, float height) {
        super(file, "none", x, y, width, height);
    }

    @Override
    public void tick(float dt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void handleMessage(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
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
