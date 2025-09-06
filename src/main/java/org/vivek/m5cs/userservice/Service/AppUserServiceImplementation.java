package org.vivek.m5cs.userservice.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vivek.m5cs.userservice.Util_Configs.AESUtil;
import org.vivek.m5cs.userservice.Model.AppUser;
import org.vivek.m5cs.userservice.Model.Role;
import org.vivek.m5cs.userservice.Repository.AppUserRepository;
import org.vivek.m5cs.userservice.Security.JWTServiceImplementation;

@Service
public class AppUserServiceImplementation implements AppUserService {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AESUtil aesUtil;
    @Autowired
    JWTServiceImplementation jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
//    @Autowired
//    ComplaintRepository complaintRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public AppUser save(AppUser appUser) {
        appUser.setPassword(passwordEncoder
                .encode(appUser.getPassword()));
        appUser.setRole(Role.USER);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser savepolice(AppUser appUser) {
        appUser.setPassword(passwordEncoder
                .encode(appUser.getPassword()));
        appUser.setRole(Role.POLICE);
        return appUserRepository.save(appUser);    }

    @Override
    public AppUser findByUsername(String username) throws Exception {
        return appUserRepository.findByUsername(username);
    }

//    public List<Complaint> findcomplaintByUser(String username) throws Exception {
//        AppUser user = findByUsername(username);
//        return complaintRepository.findByUser(user);
//    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public String validateUser(AppUser u) {
        System.out.println(1);
        try{
           String username = u.getUsername();
            System.out.println(username);
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, u.getPassword()
                )
        );
        System.out.println(2);
        if (authenticate.isAuthenticated()) {
            System.out.println(3);
            //after login send the token
            return jwtService.generateToken(username, "USER");
        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String validatePolice(AppUser u) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        u.getUsername(), u.getPassword()
                )
        );

        if (authenticate.isAuthenticated()) {
            //after login send the token
            return jwtService.generateToken(u.getUsername(), "POLICE");
        }
        return null;
    }

    @Override
    public Boolean checkuserpresent(String email, String username) throws Exception {
        AppUser user = findByEmail(email);
        return (user != null || findByUsername(username) != null);
    }

    public String getToken(String email) {
       AppUser user = findByEmail(email);
        return jwtService.generateToken(user.getUsername(), "USER");
    }

}
