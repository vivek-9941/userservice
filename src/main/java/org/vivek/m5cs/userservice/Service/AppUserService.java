package org.vivek.m5cs.userservice.Service;


import org.vivek.m5cs.userservice.Model.AppUser;

import java.util.List;

public interface AppUserService {
    int first=234;
    AppUser save(AppUser appUser);
    AppUser savepolice(AppUser appUser);
    AppUser findByUsername(String username) throws Exception;
    String getToken(String email);
//    List<Complaint> findcomplaintByUser(String username) throws Exception;
    AppUser findByEmail(String email);
    String validateUser(AppUser user);
    String validatePolice(AppUser user);
    Boolean checkuserpresent(String email,String username) throws Exception;
}
