package main.texture;

import java.util.Arrays;

/**
 * Author: FreezerburnVinny
 * Date: 1/10/12
 * Time: $(TIME}
 */
public class AnimatedTexture extends Texture {
    private Texture[] mFrames;

    private int mCurrentFrame;
    private double mCurrentTime;
    private double[] mFrameSwapTimes;

    private boolean mShouldReverse, mReversing;
    private boolean mIsPaused;
    private int mStopAt;

    public AnimatedTexture(Texture[] frames, double[] frameSwapTimes) {
        this(frames, frameSwapTimes, false);
    }

    public AnimatedTexture(Texture[] frames, double[] frameSwapTimes, boolean reverse) {
        this.mFrames = frames;
        if (frameSwapTimes.length < frames.length) {
            double[] newSwapTimes = new double[frames.length];
            System.arraycopy(frameSwapTimes, 0, newSwapTimes, 0, frameSwapTimes.length);
            Arrays.fill(newSwapTimes,
                    frameSwapTimes.length, newSwapTimes.length,
                    frameSwapTimes[frameSwapTimes.length - 1]);
            this.mFrameSwapTimes = newSwapTimes;
        } else {
            this.mFrameSwapTimes = frameSwapTimes;
        }
        this.mCurrentFrame = 0;
        this.mCurrentTime = 0.0;
        this.mShouldReverse = reverse;
        this.mReversing = false;
        this.mIsPaused = false;
        this.mStopAt = -1;
    }

    @Override
    public void alloc() {
        for (Texture t : mFrames) {
            t.alloc();
        }
    }

    @Override
    public void destroy() {
        for (Texture t : mFrames) {
            t.destroy();
        }
    }

    @Override
    public float getTexBottomRightX() {
        return mFrames[mCurrentFrame].getTexBottomRightX();
    }

    @Override
    public float getTexTopRightX() {
        return mFrames[mCurrentFrame].getTexTopRightX();
    }

    @Override
    public float getTexTopLeftX() {
        return mFrames[mCurrentFrame].getTexTopLeftX();
    }

    @Override
    public float getTexBottomLeftX() {
        return mFrames[mCurrentFrame].getTexBottomLeftX();
    }

    @Override
    public float getTexBottomRightY() {
        return mFrames[mCurrentFrame].getTexBottomRightY();
    }

    @Override
    public float getTexTopRightY() {
        return mFrames[mCurrentFrame].getTexTopRightY();
    }

    @Override
    public float getTexTopLeftY() {
        return mFrames[mCurrentFrame].getTexTopLeftY();
    }

    @Override
    public float getTexBottomLeftY() {
        return mFrames[mCurrentFrame].getTexBottomLeftY();
    }

    @Override
    public float getWidth() {
        return mFrames[mCurrentFrame].getWidth();
    }

    @Override
    public float getHeight() {
        return mFrames[mCurrentFrame].getHeight();
    }

    @Override
    public int getName() {
        return mFrames[mCurrentFrame].getName();
    }

    @Override
    public int getTarget() {
        return mFrames[mCurrentFrame].getTarget();
    }

    @Override
    public String getManagerHandle() {
        throw new UnsupportedOperationException("UNIMPL: manager handler never created for animated textures yet");
    }

    @Override
    public void setWidth(double width) {
        mFrames[mCurrentFrame].setWidth(width);
    }

    @Override
    public void setHeight(double height) {
        mFrames[mCurrentFrame].setHeight(height);
    }

    @Override
    public void bind(float dt) {
        if (mIsPaused || mCurrentFrame == mStopAt) {
            mFrames[mCurrentFrame].bind(dt);
            return;
        }

        mCurrentTime += dt;
        while (mCurrentTime >= mFrameSwapTimes[mCurrentFrame]) {
            mCurrentTime -= mFrameSwapTimes[mCurrentFrame];
            if (mShouldReverse) {
                if (mReversing) {
                    mCurrentFrame--;
                } else {
                    mCurrentFrame++;
                }
                if (mCurrentFrame == mFrames.length) {
                    mCurrentFrame -= 2;
                    mReversing = true;
                } else if (mCurrentFrame == -1) {
                    mCurrentFrame += 2;
                    mReversing = false;
                }
            } else {
                mCurrentFrame = (mCurrentFrame + 1) % mFrames.length;
            }
        }
        mFrames[mCurrentFrame].bind(dt);
    }

    @Override
    public boolean isValidTexture() {
        return mFrames[mCurrentFrame].isValidTexture();
    }

    @Override
    public void restart() {
        mCurrentFrame = 0;
    }

    @Override
    public void pause() {
        mIsPaused = true;
    }

    @Override
    public void resume() {
        mIsPaused = false;
    }

    @Override
    public void stopAt(int frame) {
        mStopAt = frame;
    }

    @Override
    public void stopAfterFullCycle() {
        mStopAt = mFrames.length - 1;
    }

    @Override
    public int numFrames() {
        return mFrames.length;
    }
}
