package com.yageum.fintech.global.util;

import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
