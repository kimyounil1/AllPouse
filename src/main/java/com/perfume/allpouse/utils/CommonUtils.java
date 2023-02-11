package com.perfume.allpouse.utils;

import com.perfume.allpouse.exception.CustomException;

import static com.perfume.allpouse.exception.ExceptionEnum.INAPPROPRIATE_PERIOD_NUM;

public class CommonUtils {

    // periodNum -> Days
    public static int periodNumToDays(int periodNum) {
        int days;

        if (periodNum == 0) {
            days = 365 * 100;
        } else if (periodNum == 1) {
            days = 7;
        } else if (periodNum == 2) {
            days = 31;
        } else if (periodNum == 3) {
            days = 183;
        } else {
            throw new CustomException(INAPPROPRIATE_PERIOD_NUM);
        }

        return days;
    }
}
