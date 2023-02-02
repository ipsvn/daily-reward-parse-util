package lol.svn.reward.parser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import lol.svn.reward.GameType;
import lol.svn.reward.exception.RewardException;
import lol.svn.reward.structs.RewardRarity;
import lol.svn.reward.structs.StreakInfo;
import lol.svn.reward.util.Translator;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public final class RewardParser {

    private RewardParser() {

    }

    private static final Pattern SECURITY_TOKEN_PATTERN = Pattern.compile(
            "window\\.securityToken = \"(.+?)\";"
    );
    private static final Pattern APPDATA_PATTERN = Pattern.compile(
            "window\\.appData = '(.+?)';"
    );
    private static final Pattern I18N_PATTERN = Pattern.compile(
            "window\\.i18n = (\\{[\\S\\s]*?});"
    );

    private static String regexFind(Pattern pattern, String input, String exceptionMessage) {
        var matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new RewardException(exceptionMessage);
        }
        return matcher.group(1);
    }

    public static RewardParseResult parse(@NotNull String html) {
        Objects.requireNonNull(html);

        var appData = new JSONObject(regexFind(
                APPDATA_PATTERN, html, "No appData in provided HTML"
        ));
        if (appData.has("error")) {
            throw new RewardException("appData contains an error: " + appData.getString("error"));
        }

        var id = appData.getString("id");

        var securityToken = regexFind(
                SECURITY_TOKEN_PATTERN, html,
                "No securityToken in provided HTML"
        );

        var i18n = new JSONObject(regexFind(
                I18N_PATTERN, html,
                "No i18n in provided HTML"
        ));
        var translator = new Translator(i18n);

        var dailyStreak = appData.getJSONObject("dailyStreak");
        var streakInfo = new StreakInfo(
                dailyStreak.getInt("highScore"),
                dailyStreak.getInt("score"),
                dailyStreak.getInt("value"),
                dailyStreak.getBoolean("keeps"),
                dailyStreak.getBoolean("token")
        );

        var rewardsJson = appData.getJSONArray("rewards");
        var options = new ArrayList<RewardParseResult.Option>();
        for (int i = 0; i < rewardsJson.length(); i++) {

            var currentOption = rewardsJson.getJSONObject(i);

            var intListJson = currentOption.optJSONArray("intlist");

            int[] intList = null;
            if (intListJson != null) {
                intList = new int[intListJson.length()];
                for (int x = 0; x < intListJson.length(); x++) {
                    intList[x] = intListJson.getInt(x);
                }
            }

            options.add(new RewardParseResult.Option(
                    i,
                    currentOption.optInt("amount", -1),
                    intList,
                    RewardRarity.valueOf(currentOption.getString("rarity")),
                    GameType.from(currentOption.optString("gameType", null)),
                    currentOption.getString("reward"),
                    currentOption.optString("package", null),
                    currentOption.optString("key", null)
            ));

        }

        return new RewardParseResult(id, securityToken, streakInfo, translator, options);

    }

}
