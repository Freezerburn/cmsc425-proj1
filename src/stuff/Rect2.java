package stuff;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 2:12 AM
 */
public class Rect2 {
    protected Vector2 topLeft, bottomRight;

    public Rect2(Vector2 topLeft, Vector2 bottomRight) {
        this.topLeft = new Vector2(topLeft.x, topLeft.y);
        this.bottomRight = new Vector2(bottomRight.x, bottomRight.y);
    }

    public float getLeft() {
        return this.topLeft.x;
    }

    public float getRight() {
        return this.bottomRight.x;
    }

    public float getBottom() {
        return this.bottomRight.y;
    }

    public float getTop() {
        return this.topLeft.y;
    }

    public boolean collidesWith(Rect2 other) {
        if(other.getLeft() > this.getRight()) return false;
        else if(other.getRight() < this.getLeft()) return false;
        else if(other.getTop() < this.getBottom()) return false;
        else if(other.getBottom() > this.getTop()) return false;
        return true;
    }
}
