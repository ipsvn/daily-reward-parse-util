package lol.svn.reward;

import lol.svn.reward.impl.HypixelRewardImpl;
import lol.svn.reward.options.BasicRewardOption;
import lol.svn.reward.options.RewardResponse;

import java.util.concurrent.CompletableFuture;

public interface HypixelReward extends AutoCloseable {

    static HypixelReward fromId(String id) {
        return HypixelRewardImpl.fromId(id);
    }

    static HypixelReward fromUrl(String url) {
        return HypixelRewardImpl.fromUrl(url);
    }

    /**
     * Retrieves the options and streak info from the main claim page.
     *
     * @return A {@link CompletableFuture<RewardResponse>} for the result of the query
     */
    CompletableFuture<RewardResponse> retrieveRewardOptions();

    /**
     * Claims the reward option selected.
     *
     * @param rewardResponse A reward response, from {@link HypixelReward#retrieveRewardOptions()}
     *                       This is required for the csrf token/cookie.
     * @param option         The reward option to select
     * @return A {@link CompletableFuture} for when the operation succeeds
     * @see BasicRewardOption#from(int)
     */
    CompletableFuture<Void> claim(RewardResponse rewardResponse, BasicRewardOption option);

    /**
     * @return The 8 character alphanumeric ID
     */
    String getId();

    /**
     * Get the URL of the website provided to pick and claim a reward card
     *
     * @return The URL of the claiming website, equal to BASE_URL + {@link HypixelReward#getId()}
     */
    String getUrl();

    @Override
    void close();
}
