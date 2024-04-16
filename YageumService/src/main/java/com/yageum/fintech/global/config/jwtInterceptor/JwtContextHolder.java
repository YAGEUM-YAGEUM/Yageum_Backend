package com.yageum.fintech.global.config.jwtInterceptor;

public class JwtContextHolder {
    private static final ThreadLocal<Long> userIdContext = new ThreadLocal<>();
    private static final ThreadLocal<String> usernameContext = new ThreadLocal<>();
    private static final ThreadLocal<String> idContext = new ThreadLocal<>();


    public static void setUserId(Long userId) {
        userIdContext.set(userId);
    }

    public static Long getUserId() {
        return userIdContext.get();
    }

    public static void setUsername(String username) {
        usernameContext.set(username);
    }

    public static String getUsername() {
        return usernameContext.get();
    }

    public static void setId(String id) {
        idContext.set(id);
    }

    public static String getId() {
        return idContext.get();
    }

    public static void clear() {
        userIdContext.remove();
        usernameContext.remove();
        idContext.remove();
    }
}
