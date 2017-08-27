package com.wanglin.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResult {
    private UserCoinInfo fromUser;
    private UserCoinInfo toUser;


    public TransactionResult() { }

    public TransactionResult(UserCoinInfo fromUser, UserCoinInfo toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    @JsonProperty
    public UserCoinInfo getFromUser() {
        return fromUser;
    }

    @JsonProperty
    public UserCoinInfo getToUser() {
        return toUser;
    }
}
