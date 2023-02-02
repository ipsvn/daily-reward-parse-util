package lol.svn.reward.util;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

/**
 * This class provides the ability to use the i18n js object to get the
 * translated display names of the reward cards
 */
public class Translator {

    private final JSONObject i18n;

    public Translator(JSONObject i18n) {
        this.i18n = i18n;
    }

    @Nullable
    public String translate(String key) {
        return translate(key, Collections.emptyMap());
    }

    @Nullable
    public String translate(String key, Map<String, String> substitutes) {

        var result = i18n.optString(key, null);
        if (result == null) {
            return null;
        }

        for (var entry : substitutes.entrySet()) {
            result = result.replaceAll("\\{\\$" + entry.getKey() + "}", entry.getValue());
        }

        return result;

    }

}
