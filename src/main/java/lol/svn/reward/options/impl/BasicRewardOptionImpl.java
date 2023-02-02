package lol.svn.reward.options.impl;

import lol.svn.reward.options.BasicRewardOption;

public class BasicRewardOptionImpl implements BasicRewardOption {

    private final int index;

    public BasicRewardOptionImpl(int index) {
        this.index = index;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public String toString() {
        return "BasicRewardOptionImpl{" +
                "index=" + index +
                '}';
    }

}
