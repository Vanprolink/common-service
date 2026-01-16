package com.ecommerce.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // Hàm static để gọi ở bất cứ đâu
    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "SYSTEM"; // Mặc định nếu không đăng nhập
        }

        return authentication.getName(); // Trả về username/id từ Token
    }
}
