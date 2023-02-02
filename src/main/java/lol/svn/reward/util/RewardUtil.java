package lol.svn.reward.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RewardUtil {

    /**
     * The timezone which is used for the daily rewards, used to determine
     * when the daily reward resets.
     */
    public static final ZoneId REWARD_TIMEZONE = ZoneId.of("America/New_York");

    /**
     * Checks whether the daily reward was claimed today, based on the time
     * provided in the {@link Instant}.
     * Simply checks whether the day on the provided instant is equal to
     * {@code Instant.now().atZone(REWARD_TIMEZONE) }
     *
     * @param instant The {@link Instant} to be checked
     * @return Whether the reward was claimed today based on the instant provided
     */
    public static boolean wasClaimedToday(Instant instant) {
        var currentTime = Instant.now().atZone(REWARD_TIMEZONE);
        return currentTime.truncatedTo(ChronoUnit.DAYS)
                .equals(instant.atZone(REWARD_TIMEZONE).truncatedTo(ChronoUnit.DAYS));
    }

}
