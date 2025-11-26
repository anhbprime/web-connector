package core;

public enum ErrorDef {
    // 공통
    BAD_REQUEST,
    NO_INPUT,
    UNKNOWN_PAGE,
    UNKNOWN_OPERATION,
    DB_ERROR,
    SERVER_ERROR,

    // 비즈니스 예시
    USER_NOT_FOUND,
    USER_SELECT_FAILED,
    ORDER_SELECT_FAILED
}
