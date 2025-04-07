package com.pfe.backendpfe.email;

import com.pfe.backendpfe.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async

    public void sendEmail(
            String to,
            EmailTemplateName emailTemplate,
            String url,
            String subject
    ) throws MessagingException {
        try {
            // Vérification initiale de l'adresse email
            if (to == null || to.isBlank()) {
                throw new IllegalArgumentException("L'adresse e-mail ne peut pas être vide.");
            }

            to = to.trim(); // Suppression des espaces inutiles

            if (!to.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Adresse email invalide : " + to);
            }

            log.info("Envoi d'un email à : '{}'", to);

            // Définition du template et des propriétés
            String templateName;
            Map<String, Object> properties = new HashMap<>();

            switch (emailTemplate) {
                case CONFIRM_EMAIL:
                    templateName = "CONFIRMATION";
                    properties.put("confirmationUrl", url);
                    break;
                case RESET_PASSWORD:
                    templateName = "RESET_PASSWORD";
                    properties.put("resetPasswordUrl", url);
                    break;
                default:
                    throw new IllegalArgumentException("Type d'email non pris en charge.");
            }

            log.info("Utilisation du template : {}", templateName);

            // Génération du contenu de l'email
            Context context = new Context();
            context.setVariables(properties);
            String emailContent = templateEngine.process(templateName, context);

            log.info("Contenu de l'email généré :\n{}", emailContent);

            // Création du message MIME
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

            helper.setFrom("contact@telnet.com"); // Vérifier que cette adresse est bien configurée
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            // Envoi du mail
            mailSender.send(mimeMessage);
            log.info("E-mail envoyé avec succès à {}", to);

        } catch (IllegalArgumentException e) {
            log.error("Erreur de validation : {}", e.getMessage());
        } catch (MessagingException e) {
            log.error("Erreur lors de l'envoi du mail à {} : {}", to, e.getMessage());
            throw e; // Propagation de l'erreur si c'est un problème SMTP
        } catch (Exception e) {
            log.error("Une erreur inattendue est survenue lors de l'envoi de l'email à {} : {}", to, e.getMessage());
        }
    }


    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }


   /* public void sendApprovalEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("contact@telnet.com"); // Set the "From" address
        message.setTo(to);
        message.setSubject("Approval Email");
        message.setText("Hello " + username + ",\n\nYour approval is successful!");
        mailSender.send(message);
        log.info("E-mail envoyé avec succès à {}", to);    }*/
   public void sendApprovalEmail(String to, String username) throws MessagingException {
       // Créez le corps de l'email en HTML
       String messageBody = "<html><body>"
               + "<p>Hello " + username + ",</p>"
               + "<p>Your account has been successfully approved!</p>"
               + "<p>Click the button below to log in to your account:</p>"
               + "<p><a href='http://localhost:4200/login' style='"
               + "background-color: #4CAF50; color: white; padding: 14px 20px; text-align: center; "
               + "text-decoration: none; display: inline-block; border-radius: 5px;'>Log In</a></p>"
               + "</body></html>";

       // Créez un SimpleMailMessage pour envoyer l'email
       MimeMessage message = mailSender.createMimeMessage();
       MimeMessageHelper helper = new MimeMessageHelper(message, true);

       try {
           // Configurez l'email
           helper.setFrom("contact@telnet.com");
           helper.setTo(to);
           helper.setSubject("Approval Email");
           helper.setText(messageBody, true); // true pour signaler que le corps du message est en HTML

           // Envoyez l'email
           mailSender.send(message);
           log.info("E-mail envoyé avec succès à {}", to);
       } catch (MessagingException e) {
           log.error("Erreur lors de l'envoi de l'email", e);
       }
   }



}
