package com.yageum.fintech.domain.auth.jwt;

public class JwtContextHolder {
    private static final ThreadLocal<Long> userIdContext = new ThreadLocal<>();
    private static final ThreadLocal<String> nameContext = new ThreadLocal<>();
    private static final ThreadLocal<String> uidContext = new ThreadLocal<>();


    public static void setUserId(Long userId) {
        userIdContext.set(userId);
    }

    public static Long getUserId() {
        return userIdContext.get();
    }

    public static void setName(String name) {
        nameContext.set(name);
    }

    public static String getName() {
        return nameContext.get();
    }

    public static void setId(String id) {
        uidContext.set(id);
    }

    public static String getId() {
        return uidContext.get();
    }

    public static void clear() {
        userIdContext.remove();
        nameContext.remove();
        uidContext.remove();
    }
}
