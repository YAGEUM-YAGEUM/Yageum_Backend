package com.yageum.fintech.global.config.jwtInterceptor;

public class JwtContextHolder {
    private static final ThreadLocal<Long> userIdContext = new ThreadLocal<>();
    private static final ThreadLocal<String> usernameContext = new ThreadLocal<>();
    private static final ThreadLocal<String> emailContext = new ThreadLocal<>();


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

    public static void setEmail(String email) {
        emailContext.set(email);
    }

    public static String getEmail() {
        return emailContext.get();
    }

    public static void clear() {
        userIdContext.remove();
        usernameContext.remove();
        emailContext.remove();
    }
}
