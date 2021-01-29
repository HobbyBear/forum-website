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

    private static String[] avators = {"http://139.198.186.81:8080/getImg/4b92d42fd26950e13662cc2dc4d6dd46", "http://139.198.186.81:8080/getImg/e801cd8e7bdc9fc10f9ba049c73576e9",
            "http://139.198.186.81:8080/getImg/cf7f3912546455d0440bd88a2b09429e", "http://139.198.186.81:8080/getImg/3a912bfbc2276c2982712d3784e139b3"};

    // 随机头像返回
    public static String randomAvatorUrl() {
        return avators[(int)(1 +Math.random() * 4)];
    }
}
