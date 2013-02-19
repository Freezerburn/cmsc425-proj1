package stuff;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/9/13
 * Time: 12:12 PM
 */
public class Vector2 {
    public float x, y;

    public Vector2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 iadd(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 isub(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector2 mult(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    public Vector2 imult(Vector2 other) {
        x *= other.x;
        y *= other.y;
        return this;
    }

    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    public Vector2 inegate() {
        x = -x;
        y = -y;
        return this;
    }

    public Vector2 smult(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public Vector2 ismult(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2 sadd(float scalar) {
        return new Vector2(x + scalar, y + scalar);
    }

    public Vector2 isadd(float scalar) {
        x += scalar;
        y += scalar;
        return this;
    }

    public float length() {
        return (float)Math.sqrt((double)(x * x + y + y));
    }

    public Vector2 normalized() {
        return smult(length());
    }
}
