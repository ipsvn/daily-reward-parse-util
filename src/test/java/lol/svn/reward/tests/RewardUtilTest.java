package lol.svn.reward.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import lol.svn.reward.util.RewardUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class RewardUtilTest {

    @Test
    void wasClaimedTodayTest () {

        final var today = Instant.now().atZone(RewardUtil.REWARD_TIMEZONE).truncatedTo(ChronoUnit.DAYS);

        assertTrue(RewardUtil.wasClaimedToday(today.plus(1, ChronoUnit.HOURS).toInstant()));
        assertTrue(RewardUtil.wasClaimedToday(today.plus(23, ChronoUnit.HOURS).toInstant()));

    }

}
