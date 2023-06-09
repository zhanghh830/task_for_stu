package com.ear.task_for_stu;

import android.text.TextUtils;

import com.ear.task_for_stu.bean.User;
import com.ear.task_for_stu.ui.vo.utils.SPUtils;

public class UserInfoHolder {
    private static UserInfoHolder eInstance = new UserInfoHolder();
    private User eUser;
    private final String KEY_USERNAME = "key_username";
    public static UserInfoHolder getInstance(){
        return eInstance;
    }
    public void setUser(User user){
        eUser = user;
        if(user != null){
            SPUtils.getInstance().put(KEY_USERNAME,user.getUserName());
        }
    }

    public User geteUser(){
        User user = eUser;
        if(user == null){
            String username = (String)SPUtils.getInstance().get(KEY_USERNAME,"");
            if(!TextUtils.isEmpty(username)){
                user = new User();
                user.setUserName(username);
            }
        }
        eUser = user;
        return user;
    }
}
