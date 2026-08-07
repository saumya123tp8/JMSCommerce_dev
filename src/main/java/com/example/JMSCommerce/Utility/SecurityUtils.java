package com.example.JMSCommerce.Utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {


//    public static Long getCurrentUserId() {
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        User user =
//                (User) authentication.getPrincipal();
//
//        return user.getId();
//    }

    public static String getCurrentUserMail() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication "+authentication);
        String email = authentication.getPrincipal().toString();
        return email;
    }
}
