package main.java.core;

import java.sql.SQLException;

public class OpException extends RuntimeException {
    private final ErrorDef def;
    private final String detailCode;

    public OpException(ErrorDef def, String detailCode, Throwable e) {
        super(e);
        this.def = def;
        if (e instanceof SQLException) {
            String sqlCode = String.valueOf(((SQLException) e).getErrorCode());
            if (detailCode == null || detailCode.isEmpty()) {
                this.detailCode = sqlCode;
            } else {
                this.detailCode = detailCode + "," + sqlCode;
            }
        } else {
            this.detailCode = detailCode;
        }
    }

    public OpException(ErrorDef def, String detailCode) {
        super();
        this.def = def;
        this.detailCode = detailCode;
    }

    public OpException(ErrorDef def, String detailCode, String detailMsg) {
        super(detailMsg);
        this.def = def;
        this.detailCode = detailCode;
    }

    public ErrorDef getDef() {
        return def;
    }

    public String getDetailCode() {
        return detailCode;
    }
}
