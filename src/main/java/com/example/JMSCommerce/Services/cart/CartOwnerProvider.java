package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.Utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartOwnerProvider {


    public String getOwnerId() {

//        User user =
        String email = SecurityUtils.getCurrentUserMail();
        return "user:" + email;
//        return "user:" + user.getId();

    }

}