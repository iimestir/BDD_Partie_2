package model;

import common.Utils;

public enum AccountType {
    USER("account_user"),
    EPIDEMIOLOGIST("account_epidemiologist");

    private final String key;

    AccountType(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return Utils.getTranslatedString(this.key);
    }
}
