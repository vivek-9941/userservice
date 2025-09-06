package org.vivek.m5cs.userservice.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="EmailSender")
public interface OtpService {
    @PostMapping("/email/save")
    public void saveOtp(@RequestParam String email, @RequestParam String otp);
    @PostMapping("/email/verify")
    public Boolean verifyOtp(@RequestParam String email,  @RequestParam String otp);
    @DeleteMapping("/email/delete")
    public void deleteOtp(@RequestParam String email);
    @PostMapping("/email/send")
    boolean sendEmail(@RequestParam String email,
                      @RequestParam String subject,
                      @RequestParam String body);
}
