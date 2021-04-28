package model;

import common.Utils;

public enum SQLRequest {
    SELECT("sql_select"),
    INSERT("sql_insert"),
    UPDATE("sql_update"),
    DELETE("sql_delete");

    private final String key;

    SQLRequest(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return Utils.getTranslatedString(this.key);
    }
}
