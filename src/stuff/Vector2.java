package stuff;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/9/13
 * Time: 12:12 PM
 */
public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }
}
