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

    private static String[] avators = {"http://qlsu57ex5.hn-bkt.clouddn.com/boy.png", "http://qlsu57ex5.hn-bkt.clouddn.com/guai.png",
            "http://qlsu57ex5.hn-bkt.clouddn.com/guai2.jfif", "http://qlsu57ex5.hn-bkt.clouddn.com/panghu.png"};

    // 随机头像返回
    public static String randomAvatorUrl() {
        return avators[(int)(1 +Math.random() * 4)];
    }
}
