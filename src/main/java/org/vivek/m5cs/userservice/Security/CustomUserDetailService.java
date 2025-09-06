package org.vivek.m5cs.userservice.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vivek.m5cs.userservice.Model.AppUser;
import org.vivek.m5cs.userservice.Repository.AppUserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
//    @Autowired
//    private AppUserService service;

    @Autowired
    private AppUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user =  repository.findByUsername(username);
        System.out.println(user);
        if(user==null) {
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        else{
            return new userdetails(user);
        }
    }
}
