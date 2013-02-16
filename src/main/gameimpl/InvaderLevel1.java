package main.gameimpl;

import main.Game;
import stuff.Rect2;
import stuff.Utils;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: freezerburn
 * Date: 2/15/13
 * Time: 1:24 AM
 */
public class InvaderLevel1 implements InvadersLevel {
//    protected static int rows = 1;
    protected static int rows = 5;
//    protected static int columns = 1;
    protected static int columns = 8;
    protected static final float DISTANCE_BETWEEN_X = 8.0f;
    protected static final float DISTANCE_BETWEEN_Y = 5.0f;
    protected static final double FIRE_CHANCE = 0.00001;

    protected float totalTime = 0.0f;
    protected LinkedList<InvaderEnemy> invaders = new LinkedList<InvaderEnemy>();

    @Override
    public void initInvaders() {
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < columns; x++) {
                InvaderEnemy invader = new InvaderEnemy(3.0f + x * (DISTANCE_BETWEEN_X + InvaderEnemy.INVADER_WIDTH),
                        Game.windowHeight - 3.0f - InvaderEnemy.INVADER_HEIGHT - y * (DISTANCE_BETWEEN_Y + InvaderEnemy.INVADER_HEIGHT) ,
                        true);
                invaders.push(invader);
            }
        }
    }

    @Override
    public boolean levelFinished() {
        return invaders.isEmpty();
    }

    @Override
    public void tick(float dt) {
        this.totalTime += dt;

        boolean shouldReverse = false;
        ListIterator it = invaders.listIterator();
        while(it.hasNext()) {
            InvaderEnemy invader = (InvaderEnemy)it.next();
            if(invader.isDestroyed()) {
                it.remove();
            }
            else {
                Rect2 bounds = invader.getBounds();
                if(Utils.random.nextGaussian() < FIRE_CHANCE) {
                    invader.handleMessage(InvaderEnemy.FIRE);
                }
                if(bounds.getLeft() <= 0.0f) {
                    shouldReverse = true;
                    break;
                }
                else if(bounds.getRight() >= Game.windowWidth) {
                    shouldReverse = true;
                    break;
                }
            }
        }
        if(shouldReverse) {
            for(InvaderEnemy invader : invaders) {
                invader.handleMessage(InvaderEnemy.REVERSE_DIRECTION);
            }
        }
    }
}
