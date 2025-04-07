package com.pfe.backendpfe.email;

import com.pfe.backendpfe.auth.AuthenticationService;
import com.pfe.backendpfe.user.User;
import com.pfe.backendpfe.user.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/test")
public class MailTestController {
    private final JavaMailSender mailSender;

    public MailTestController(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }

    @GetMapping("/check-mail")
    public String checkMailBean() {
        return (mailSender != null) ? "JavaMailSender is loaded!" : "JavaMailSender is null!";
    }


    }




