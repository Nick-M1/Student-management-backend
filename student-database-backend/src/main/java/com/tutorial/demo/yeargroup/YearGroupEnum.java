package com.tutorial.demo.yeargroup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum YearGroupEnum {
    YEAR_1(1),
    YEAR_2(2),
    YEAR_3(3),
    YEAR_4(4),
    YEAR_5(5);

    private final int numVal;

    YearGroupEnum(int numVal) {
        this.numVal = numVal;
    }

    @JsonCreator
    public static YearGroupEnum decode(final int numVal) {
        return Stream.of(YearGroupEnum.values()).filter(targetEnum -> targetEnum.numVal == numVal).findFirst().orElse(null);
    }

    @JsonCreator
    public static YearGroupEnum decode(final String numVal) {
        int numValInt = Integer.parseInt(numVal);
        return Stream.of(YearGroupEnum.values()).filter(targetEnum -> targetEnum.numVal == numValInt).findFirst().orElse(null);
    }

    @JsonValue
    public int getNumVal() {
        return numVal;
    }
}
