package com.edu.ulab.app.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum IntConstants {
    PERSON_UNREAL_AGE_INT(0),
    USER_ID_NOT_FOUND_INT(0),
    SMTHNG_GOES_WRONG_CONNECTION_TO_DB(-1),
    BOOK_ID_NOT_FOUND_INT(0),
    BOOK_UNREAL_PAGE_QTY_INT(0);
    private final int digits;

    IntConstants(int digits)
    {
        this.digits = digits;
    }
}
