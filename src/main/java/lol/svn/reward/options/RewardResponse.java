package lol.svn.reward.options;

import lol.svn.reward.HypixelReward;
import lol.svn.reward.options.impl.RewardOptionImpl;
import lol.svn.reward.parser.RewardParseResult;
import lol.svn.reward.structs.StreakInfo;
import lol.svn.reward.util.Translator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RewardResponse {

    private final HypixelReward rewardClient;
    private final String id, securityToken;
    private final String csrfCookie;
    private final StreakInfo streakInfo;
    private final Translator translator;
    private final List<RewardOption> options;

    public RewardResponse(HypixelReward rewardClient,
                          String id, String csrfCookie, String securityToken,
                          StreakInfo streakInfo, Translator translator,
                          List<RewardOption> options) {
        this.rewardClient = rewardClient;
        this.id = id;
        this.csrfCookie = csrfCookie;
        this.securityToken = securityToken;
        this.streakInfo = streakInfo;
        this.translator = translator;
        this.options = options;
    }

    public RewardResponse(HypixelReward rewardClient, String csrfCookie, RewardParseResult parseResult) {
        this.rewardClient = rewardClient;
        this.csrfCookie = csrfCookie;
        this.id = parseResult.getId();
        this.securityToken = parseResult.getSecurityToken();
        this.streakInfo = parseResult.getStreakInfo();
        this.translator = parseResult.getTranslator();
        this.options = parseResult.getOptions().stream()
                .map(option -> new RewardOptionImpl(this, option))
                .collect(Collectors.toList());
    }

    public CompletableFuture<Void> claim(BasicRewardOption option) {
        return rewardClient.claim(this, option);
    }

    public String getId() {
        return id;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public String getCsrfCookie() {
        return csrfCookie;
    }

    public StreakInfo getStreakInfo() {
        return streakInfo;
    }

    public Translator getTranslator() {
        return translator;
    }

    public List<RewardOption> getOptions() {
        return options;
    }

}
