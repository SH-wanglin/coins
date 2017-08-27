package com.wanglin.model;


public class UserCoin {
    private long userId;
    private long coins;

    public UserCoin() { }

    public UserCoin(long userId, long coins)
    {
        this.userId = userId;
        this.coins = coins;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }
}
