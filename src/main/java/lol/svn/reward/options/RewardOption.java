package lol.svn.reward.options;

import org.jetbrains.annotations.Nullable;
import lol.svn.reward.GameType;
import lol.svn.reward.structs.RewardRarity;

import java.util.concurrent.CompletableFuture;

public interface RewardOption extends BasicRewardOption {

    /**
     * Claims the reward option selected.
     *
     * @return A {@link CompletableFuture} for when the operation succeeds
     */
    CompletableFuture<Void> claim();

    int getAmount();

    RewardRarity getRarity();

    GameType getGameType();

    String getRewardRaw();

    @Nullable
    String getRewardPackageRaw();

    @Nullable
    String getRewardKeyRaw();

    String getDisplayName();

    String getDisplayAmountOrType();

}
