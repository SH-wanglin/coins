package com.wanglin.constant;

public enum CoinType {
    CHARGE(1, "充值"),
    TRANSFER(2, "转账");

    private final int typeId;
    private final String typeDesc;

    private CoinType(int typeId, String typeDesc) {
        this.typeId = typeId;
        this.typeDesc = typeDesc;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public static String geTypeDesc(int typeId) {
        switch (typeId) {
            case 1:
                return CHARGE.typeDesc;
            case 2:
                return TRANSFER.typeDesc;
        }
        return null;
    }

}
