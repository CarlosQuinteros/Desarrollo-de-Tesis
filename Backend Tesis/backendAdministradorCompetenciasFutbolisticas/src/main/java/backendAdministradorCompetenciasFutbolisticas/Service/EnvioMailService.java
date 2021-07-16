package backendAdministradorCompetenciasFutbolisticas.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnvioMailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String destinatario, String asunto, String contenido) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(destinatario);
        email.setSubject(asunto);
        email.setText(contenido);

        mailSender.send(email);
    }
}
