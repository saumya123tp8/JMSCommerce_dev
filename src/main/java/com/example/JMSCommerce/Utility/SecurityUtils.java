package com.example.JMSCommerce.Utility;

import com.example.JMSCommerce.Model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        return user.getId();
    }
}
