package com.blog.template.common.utils;

public class UserUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getUserId() {
        return threadLocal.get();
    }

    public static void setUserId(Long id) {
        threadLocal.set(id);
    }

    public static void removeUserId() {
        threadLocal.remove();
    }
}
