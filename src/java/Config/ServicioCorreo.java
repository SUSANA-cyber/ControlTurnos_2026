package Config;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServicioCorreo {

    public static boolean enviarEmail(String destinatario, String asunto, String cuerpo) {

        final String correoAdmin = "juiopacp@gmail.com";
        final String passwordApp = "gtlzpiysxgeiazju";

        System.out.println("======================================");
        System.out.println("INTENTANDO ENVIAR CORREO");
        System.out.println("Remitente: " + correoAdmin);
        System.out.println("Destinatario: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("======================================");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoAdmin, passwordApp);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoAdmin));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);

            System.out.println("CORREO ENVIADO EXITOSAMENTE A: " + destinatario);
            return true;

        } catch (MessagingException e) {
            System.out.println("ERROR AL ENVIAR CORREO A: " + destinatario);
            e.printStackTrace();
            return false;
        }
    }
}
