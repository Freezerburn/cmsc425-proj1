package main.gameimpl;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 9:14 PM
 */
public class HealthHeart extends BaseEntity {
    public static final String HEART_TEX = "res/heart.png";
    public static final float HEART_WIDTH = 19.0f;
    public static final float HEART_HEIGHT = 19.0f;
    public static final String ENT_NAME = "heart";

    public HealthHeart(float x, float y) {
        super(HEART_TEX, "none", x, y, HEART_WIDTH, HEART_HEIGHT);
        this.layer++;
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
        return ENT_NAME;
    }

    @Override
    public String getCollisionType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onContextEnter() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onContextLeave() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
