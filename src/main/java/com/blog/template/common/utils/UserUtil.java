package com.blog.template.common.utils;

import com.blog.template.models.userinfo.UserInfo;

public class UserUtil {

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static UserInfo getUser() {
        return threadLocal.get();
    }

    public static void setUser(UserInfo userInfo) {
        threadLocal.set(userInfo);
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
