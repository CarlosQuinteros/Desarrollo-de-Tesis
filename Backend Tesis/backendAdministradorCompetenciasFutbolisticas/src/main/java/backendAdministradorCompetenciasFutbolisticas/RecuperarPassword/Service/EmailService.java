package backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Service;

import backendAdministradorCompetenciasFutbolisticas.RecuperarPassword.Dto.EmailValuesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    @Value("${mail.urlFront}")
    private String urlFront;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    public void sendEmail(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(this.mailFrom);
        mailMessage.setTo("admcompetenciasfutbolisticas@gmail.com");
        mailMessage.setSubject("Prueba de envio email simple");
        mailMessage.setText("Este es el contenido del mail");

        javaMailSender.send(mailMessage);
    }

    public void sendEmailConPlantilla(EmailValuesDto emailValuesDto){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            Context context  = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("userName", emailValuesDto.getUserName());
            model.put("url", this.urlFront + emailValuesDto.getToken());
            context.setVariables(model);

            String htmlText = templateEngine.process("email-template", context);
            helper.setFrom(this.mailFrom);
            helper.setTo(emailValuesDto.getMailTo());
            helper.setSubject("Prueba de envio email con plantilla");
            helper.setText(htmlText,true);
            javaMailSender.send(message);


        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
