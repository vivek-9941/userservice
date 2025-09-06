package org.vivek.m5cs.userservice.Util_Configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vivek.m5cs.userservice.Model.AppUser;
import org.vivek.m5cs.userservice.Service.AppUserService;

@Component
public class Utility_class {
    @Autowired
    AESUtil util;
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }

        return authentication.getName();
    }

    public AppUser getCurrentUser(AppUserService appUserService) throws Exception {
//        String username = util.encryptPlainText(getCurrentUsername());
        return appUserService.findByUsername(getCurrentUsername());

    }
}