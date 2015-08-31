package com.joysoft.andutils.common;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fengmiao on 15/9/1.
 */
public class Data {

    public static boolean isValid(List obj){
        return obj != null && obj.size() > 0;
    }

    public static boolean isValid(Map obj){
        return obj != null && obj.size() > 0;
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }
}
