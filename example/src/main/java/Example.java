import lol.svn.reward.HypixelReward;
import lol.svn.reward.options.RewardOption;

public class Example {

    public static void main(String[] args) {

        try (var reward = HypixelReward.fromId("aaaaaaaa")) {
            reward.retrieveRewardOptions()
                    .thenApply(response -> {
                        response.getOptions().forEach(rewardOption
                                -> System.out.println(rewardOption.getDisplayName() + " | " + rewardOption.getDisplayAmountOrType()));
                        return response;
                    })
                    .thenApply(response -> response.getOptions().get(0))
                    .thenCompose(RewardOption::claim)
                    .thenAccept(unused -> System.out.println("Claimed"))
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    })
                    .join();
        }

    }

}
