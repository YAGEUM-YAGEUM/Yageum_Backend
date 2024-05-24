package com.yageum.fintech.domain.auth.jwt;

public class JwtContextHolder {
    private static final ThreadLocal<Long> uidContext = new ThreadLocal<>(); //사용자 UID
    private static final ThreadLocal<String> nameContext = new ThreadLocal<>(); //사용자 이름
    private static final ThreadLocal<String> usernameContext = new ThreadLocal<>(); //사용자 아이디

    public static void setUid(Long uid) {
        uidContext.set(uid);
    }

    public static Long getUid() {
        return uidContext.get();
    }

    public static void setName(String name) {
        nameContext.set(name);
    }

    public static String getName() {
        return nameContext.get();
    }

    public static void setUsername(String id) {
        usernameContext.set(id);
    }

    public static String getUsername() {
        return usernameContext.get();
    }

    public static void clear() {
        uidContext.remove();
        nameContext.remove();
        usernameContext.remove();
    }
}
