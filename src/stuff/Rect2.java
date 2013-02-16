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
//        System.out.println("First: 2l:" + other.getLeft() + ", 1r:" + this.getRight());
//        System.out.println("Second: 2r:" + other.getRight() + ", 1l:" + this.getLeft());
//        System.out.println("Third: 2t:" + other.getTop() + ", 1b:" + this.getBottom());
//        System.out.println("Fourth: 2b:" + other.getBottom() + ", 1t:" + this.getTop());
        if(other.getLeft() > this.getRight()) return false;
        else if(other.getRight() < this.getLeft()) return false;
        else if(other.getTop() < this.getBottom()) return false;
        else if(other.getBottom() > this.getTop()) return false;
        return true;
    }
}
