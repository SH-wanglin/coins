package com.wanglin.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCoinInfo {
    private long userId;
    private long coins;

    public UserCoinInfo() { }

    public UserCoinInfo(long userId, long coins) {
        this.userId = userId;
        this.coins = coins;
    }

    @JsonProperty
    public long getUserId() {
        return userId;
    }

    @JsonProperty
    public long getCoins() {
        return coins;
    }
}
