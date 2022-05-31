package com.rw.colormemoryhelper;

/**
 * Created by Rakhita on 2/17/2018.
 */

public class Score {

    private int mCurrentScore;
    private int mMatchingCount;

    private static final int MATCH = 2;
    private static final int NOT_MATCH = -1;

    private static Score mScore;

    private Score() {
    }

    public static Score Instance() {
        if (mScore == null)
            mScore = new Score();
        return mScore;
    }

    public int getCurrentScore() {
        return mCurrentScore;
    }

    public void plusCurrentScore() {
        this.mCurrentScore = this.mCurrentScore + MATCH;
    }

    public void minusCurrentScore() {
        this.mCurrentScore = this.mCurrentScore + NOT_MATCH;
    }

    public int getMatchingCount() {
        return mMatchingCount;
    }

    public void setMatchingCount(int mMatchingCount) {
        this.mMatchingCount = this.mMatchingCount + mMatchingCount;
    }

    public void reset(){
        mCurrentScore = 0;
        mMatchingCount = 0;
        if (mScore != null)
            mScore = null;
    }
}
