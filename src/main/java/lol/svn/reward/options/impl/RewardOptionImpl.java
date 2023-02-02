package lol.svn.reward.options.impl;

import org.jetbrains.annotations.Nullable;
import lol.svn.reward.GameType;
import lol.svn.reward.options.RewardOption;
import lol.svn.reward.options.RewardResponse;
import lol.svn.reward.parser.RewardParseResult;
import lol.svn.reward.structs.RewardRarity;
import lol.svn.reward.util.Translator;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class RewardOptionImpl implements RewardOption {

    private static final Pattern SUIT_KEY_PATTERN = Pattern.compile(
            "^[a-z\\d_]+_([a-z]+)$", Pattern.CASE_INSENSITIVE
    );


    private final RewardResponse response;
    private final int index, amount;
    private final int[] intList;
    private final RewardRarity rarity;
    private final GameType gameType;
    private final String rewardRaw;
    private final @Nullable String rewardPackageRaw, rewardKeyRaw;

    public RewardOptionImpl(RewardResponse response, int index, int amount, int[] intList,
                            RewardRarity rarity, GameType gameType, String rewardRaw,
                            @Nullable String rewardPackageRaw, @Nullable String rewardKeyRaw) {
        this.response = response;
        this.index = index;
        this.amount = amount;
        this.rarity = rarity;
        this.intList = intList;
        this.gameType = gameType;
        this.rewardRaw = rewardRaw;
        this.rewardPackageRaw = rewardPackageRaw;
        this.rewardKeyRaw = rewardKeyRaw;
    }

    public RewardOptionImpl(RewardResponse response, RewardParseResult.Option parseResult) {
        this(
                response,
                parseResult.getIndex(),
                parseResult.getAmount(),
                parseResult.getIntList(),
                parseResult.getRarity(),
                parseResult.getGameType(),
                parseResult.getRewardRaw(),
                parseResult.getRewardPackageRaw(),
                parseResult.getRewardKeyRaw()
        );
    }

    @Override
    public CompletableFuture<Void> claim() {
        return response.claim(this);
    }

    public int index() {
        return index;
    }

    public int getAmount() {
        return amount;
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

    public @Nullable String getRewardPackageRaw() {
        return rewardPackageRaw;
    }

    public @Nullable String getRewardKeyRaw() {
        return rewardKeyRaw;
    }

    public Translator getTranslator() {
        return response.getTranslator();
    }

    public String getDisplayName() {

        var translator = getTranslator();

        if ("housing_package".equals(rewardRaw)) {
            return translator.translate("housing." + getHousingCategory(this.rewardPackageRaw));
        }

        if ("add_vanity".equals(rewardRaw)) {
            if (rewardKeyRaw != null && rewardKeyRaw.contains("suit")) {
                return translator.translate("vanity." + rewardKeyRaw.replaceAll("_([a-z]+)$", ""));
            } else {
                return translator.translate("vanity." + rewardKeyRaw);
            }
        }

        if ("coins".equals(rewardRaw) || "tokens".equals(rewardRaw)) {
            return translator.translate("type." + rewardRaw, Map.of("game", gameType.getName()));
        }

        return translator.translate("type." + rewardRaw);

    }

    public String getDisplayAmountOrType() {

        var translator = getTranslator();

        if ("add_vanity".equals(rewardRaw)) {
            if (rewardKeyRaw != null && rewardKeyRaw.contains("suit")) {
                var matcher = SUIT_KEY_PATTERN.matcher(rewardKeyRaw);
                if (matcher.find()) {
                    return translator.translate("vanity.armor." + matcher.group(1));
                }
            } else if (rewardKeyRaw != null && (rewardKeyRaw.contains("emote") || rewardKeyRaw.contains("taunt"))) {
                return translator.translate("vanity." + this.rewardKeyRaw);
            }
        }

        if ("housing_package".equals(rewardRaw)) {
            return rewardPackageRaw != null
                    ? translator.translate("housing.skull." + rewardPackageRaw.replace("specialoccasion_reward_card_skull_", ""))
                    : null;
        }

        if (amount >= 0)
            return Integer.toString(amount);

        if (intList != null)
            return Integer.toString(intList.length);

        return "";

    }

    private static final Map<String, Set<String>> HOUSING_CATEGORIES = Map.of(
            "red_treasure_chests", Set.of(
                    "specialoccasion_reward_card_skull_red_treasure_chest",
                    "specialoccasion_reward_card_skull_gold_nugget",
                    "specialoccasion_reward_card_skull_pot_o'_gold",
                    "specialoccasion_reward_card_skull_rubik's_cube",
                    "specialoccasion_reward_card_skull_piggy_bank",
                    "specialoccasion_reward_card_skull_health_potion"
            ),
            "green_treasure_chest", Set.of(
                    "specialoccasion_reward_card_skull_green_treasure_chest",
                    "specialoccasion_reward_card_skull_coin_bag",
                    "specialoccasion_reward_card_skull_ornamental_helmet",
                    "specialoccasion_reward_card_skull_pocket_galaxy",
                    "specialoccasion_reward_card_skull_mystic_pearl",
                    "specialoccasion_reward_card_skull_agility_potion"
            ),
            "blue_treasure_chest", Set.of(
                    "specialoccasion_reward_card_skull_blue_treasure_chest",
                    "specialoccasion_reward_card_skull_golden_chalice",
                    "specialoccasion_reward_card_skull_jewelery_box",
                    "specialoccasion_reward_card_skull_crown",
                    "specialoccasion_reward_card_skull_molten_core",
                    "specialoccasion_reward_card_skull_mana_potion"
            )
    );

    protected static String getHousingCategory(String input) {
        return HOUSING_CATEGORIES.entrySet().stream()
                .filter(entry -> entry.getValue().contains(input))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public String toString() {
        return "RewardOptionImpl{" +
                ", index=" + index +
                ", amount=" + amount +
                ", intList=" + Arrays.toString(intList) +
                ", rarity=" + rarity +
                ", gameType=" + gameType +
                ", rewardRaw='" + rewardRaw + '\'' +
                ", rewardPackageRaw='" + rewardPackageRaw + '\'' +
                ", rewardKeyRaw='" + rewardKeyRaw + '\'' +
                '}';
    }
}
