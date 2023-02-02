package lol.svn.reward.options;

import lol.svn.reward.options.impl.BasicRewardOptionImpl;

public interface BasicRewardOption {

    static BasicRewardOption from(int index) {
        return new BasicRewardOptionImpl(index);
    }

    /**
     * @return The position of the reward card in the page (starting from 0)
     */
    int index();

}
