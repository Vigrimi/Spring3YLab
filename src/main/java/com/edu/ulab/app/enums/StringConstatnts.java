package com.edu.ulab.app.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum StringConstatnts {
    INPUTED_BOOK_PAGE_QTY_IS_UNREAL("INPUTED_BOOK_PAGE_QTY_IS_UNREAL (book must have more than zero pages)"),
    INPUTED_BOOK_ID_WAS_NOT_FOUND_IN_DB("INPUTED_BOOK_ID_WAS_NOT_FOUND_IN_DB"),
    INPUTED_BOOK_ID_IS_UNREAL("INPUTED_BOOK_ID_IS_UNREAL (is less or equal by ZERO)"),
    INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB("INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB"),
    INPUTED_USER_FULL_NAME_IS_EMPTY("INPUTED_USER_FULL_NAME_IS_EMPTY"),
    INPUTED_USER_TITLE_IS_EMPTY("INPUTED_USER_TITLE_IS_EMPTY"),
    USER_WAS_NOT_FOUND_IN_DB_BY_INPUTED_DATA("USER_WAS_NOT_FOUND_IN_DB_BY_INPUTED_DATA"),
    USER_HAS_NO_ANY_BOOK_IN_HIS_OWN_CASE("USER_HAS_NO_ANY_BOOKS_IN_HIS_OWN_CASE"),
    INPUTED_USER_UPDATED_AGE_IS_UNREAL("INPUTED_USER_UPDATED_AGE_IS_UNREAL (real human age have to be beetwen 5 " +
            "and 125 years old)"),
    INPUTED_USER_ID_IS_UNREAL("INPUTED_USER_ID_IS_UNREAL (is less or equal by ZERO)"),
    SMTHNG_GOES_WRONG_WITH_CONNECTING_DB("SMTHNG_GOES_WRONG_WITH_CONNECTING_DB");

    private final String text;
    StringConstatnts(String text){this.text = text;}
}