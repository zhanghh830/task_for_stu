package com.ear.task_for_stu.ui.vo.utils;

import com.google.gson.Gson;

public class GsonUtil {

    public static Gson eGson = new Gson();

    public static Gson getGson() {
        return eGson;
    }

}
