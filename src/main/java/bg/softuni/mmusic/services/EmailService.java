package bg.softuni.mmusic.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendActivationEmail(String userEmail, String username, String verify_code, String userUuid) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom("mmusic@gmail.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Welcome to Mmusic!");
            mimeMessageHelper.setText(generateEmailText(username, verify_code, userUuid), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateEmailText(String username, String verifyCode, String userUuid) {
        Context context = new Context();
        context.setLocale(Locale.getDefault());
        context.setVariable("username", username);

        String request = String.format("http://localhost:8080/users/%s/verify-code/%s", userUuid, verifyCode);
        context.setVariable("request", request);
        return templateEngine.process("email/registration-email", context);
    }
}
