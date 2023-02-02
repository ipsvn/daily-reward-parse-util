package lol.svn.reward.exception;

public class RewardParseException extends RewardException {

    public RewardParseException() {
    }

    public RewardParseException(String message) {
        super(message);
    }

    public RewardParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RewardParseException(Throwable cause) {
        super(cause);
    }

    public RewardParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
