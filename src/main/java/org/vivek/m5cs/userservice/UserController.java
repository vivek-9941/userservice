package org.vivek.m5cs.userservice;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vivek.m5cs.userservice.Model.AppUser;
import org.vivek.m5cs.userservice.Service.AppUserService;
import org.vivek.m5cs.userservice.Service.JWTService;
import org.vivek.m5cs.userservice.Service.OtpService;
import org.vivek.m5cs.userservice.Util_Configs.Utility_class;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/sendotp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        if (appUserService.findByEmail(email) == null) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        String subject = "Your OTP for e-FIR";
        String body = "<p>Dear user,</p>" +
                "<p>Your OTP is: <strong>" + "<h2>"+otp +"</h2>"+ "</strong></p>" +
                "<p>Valid for 2 minutes. Do not share it.</p>" +
                "<p>- e-FIR Team</p>";

        boolean verdict = otpService.sendEmail(email, subject, body);
        System.out.println(verdict);
        if (verdict) {
            otpService.saveOtp(email, otp); // Save to Redis with TTL
            return ResponseEntity.ok().body("OTP sent successfully");
        } else {
            return ResponseEntity.badRequest().body("OTP not sent");
        }
    }

    @GetMapping("/getuser")
    public AppUser getuser(@RequestParam String username) throws Exception {
        return appUserService.findByUsername(username);
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        System.out.println(isValid);
        if (isValid) {
            AppUser appUser = appUserService.findByEmail(email);
            //email verification done
            appUser.setVerified(true);
            appUserService.save(appUser);
            otpService.deleteOtp(email); // Optional: clean up manually
            String token = appUserService.getToken(email);
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUser user) throws Exception {
        System.out.println(user);
        System.out.println(0);
        if (appUserService.checkuserpresent(user.getEmail(), user.getUsername())) {
            System.out.println(1);
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("User already exists");
        } else {
            System.out.println(2 );
            AppUser saveduser = appUserService.save(user);
            System.out.println(user);
            if (saveduser != null) {
                return ResponseEntity.ok().body(saveduser);
            } else {
                return ResponseEntity.badRequest().body("Something went wrong");
            }
        }
    }

    @GetMapping("/get")
    public AppUser getUser(@RequestParam String token) throws Exception {
        System.out.println(token);
        System.out.println(jwtService.extractUserName(token));
        AppUser user = appUserService.findByUsername(jwtService.extractUserName(token));
        System.out.println(user);
        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUser user) {
        System.out.println(user);
        String token = appUserService.validateUser(user);
        System.out.println(token);
        if (token != null) {
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().body("Username or password is incorrect");
        }
    }

    @PostMapping("/login/police")
    public ResponseEntity<?> loginPolice(@RequestBody AppUser user) {
        System.out.println(user);
        System.out.println(1);
        String token = appUserService.validatePolice(user);
        System.out.println(token);
        if (token != null) {
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().body("Username or password is incorrect");
        }
    }

    @PostMapping("/register/police")
    public ResponseEntity<?> registerPolice(@RequestBody AppUser user) throws Exception {

        if (appUserService.checkuserpresent(user.getEmail(), user.getUsername())) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("User already exists");
        } else {
            System.out.println(user);
            AppUser saveduser = appUserService.savepolice(user);
            System.out.println(user);
            if (saveduser != null) {
                return ResponseEntity.ok().body(saveduser);
            } else {
                return ResponseEntity.badRequest().body("Something went wrong");
            }
        }
    }
}