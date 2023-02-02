package lol.svn.reward.parser;

import org.jetbrains.annotations.Nullable;
import lol.svn.reward.GameType;
import lol.svn.reward.structs.RewardRarity;
import lol.svn.reward.structs.StreakInfo;
import lol.svn.reward.util.Translator;

import java.util.List;

public class RewardParseResult {

    public static class Option {

        private final int index, amount;
        private final int[] intList;
        private final RewardRarity rarity;
        private final GameType gameType;
        private final String rewardRaw;
        private final @Nullable String rewardPackageRaw, rewardKeyRaw;

        public Option(int index, int amount, int[] intList,
                      RewardRarity rarity, GameType gameType, String rewardRaw,
                      @Nullable String rewardPackageRaw, @Nullable String rewardKeyRaw) {
            this.index = index;
            this.amount = amount;
            this.intList = intList;
            this.rarity = rarity;
            this.gameType = gameType;
            this.rewardRaw = rewardRaw;
            this.rewardPackageRaw = rewardPackageRaw;
            this.rewardKeyRaw = rewardKeyRaw;
        }

        public int getIndex() {
            return index;
        }

        public int getAmount() {
            return amount;
        }

        public int[] getIntList() {
            return intList;
        }

        public RewardRarity getRarity() {
            return rarity;
        }

        public GameType getGameType() {
            return gameType;
        }

        public String getRewardRaw() {
            return rewardRaw;
        }

        @Nullable
        public String getRewardPackageRaw() {
            return rewardPackageRaw;
        }

        @Nullable
        public String getRewardKeyRaw() {
            return rewardKeyRaw;
        }
    }

    private final String id;
    private final String securityToken;
    private final StreakInfo streakInfo;
    private final Translator translator;
    private final List<Option> options;

    public RewardParseResult(String id, String securityToken,
                             StreakInfo streakInfo, Translator translator,
                             List<Option> options) {
        this.id = id;
        this.securityToken = securityToken;
        this.streakInfo = streakInfo;
        this.translator = translator;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public StreakInfo getStreakInfo() {
        return streakInfo;
    }

    public Translator getTranslator() {
        return translator;
    }

    public List<Option> getOptions() {
        return options;
    }

}
