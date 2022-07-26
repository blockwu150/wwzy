package com.enation.app.javashop.framework.context.user;

import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 默认用户holder
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-05-28
 */
@Component
public class DefaultUserHolder implements  UserHolder{

    @Override
    public Seller getSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object someOne = authentication.getDetails();
        if (someOne != null && someOne instanceof Seller) {
            return (Seller) someOne;
        }
        return null;
    }

    @Override
    public Buyer getBuyer() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        //如果含有买家权限则读取卖家信息并返回
        Object someOne = authentication.getDetails();
        if (someOne != null && someOne instanceof Buyer) {
            return (Buyer) someOne;
        }

        return null;
    }
}
