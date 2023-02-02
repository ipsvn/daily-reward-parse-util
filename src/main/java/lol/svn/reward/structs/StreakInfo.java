package lol.svn.reward.structs;

public class StreakInfo {

    private final int highScore, score, value;
    private final boolean keeps, token;

    public StreakInfo(int highScore, int score, int value, boolean keeps, boolean token) {
        this.highScore = highScore;
        this.score = score;
        this.value = value;
        this.keeps = keeps;
        this.token = token;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getScore() {
        return score;
    }

    public int getValue() {
        return value;
    }

    public boolean isKeeps() {
        return keeps;
    }

    /**
     * @return Whether a reward token has been redeemed to create this session
     */
    public boolean isToken() {
        return token;
    }

}