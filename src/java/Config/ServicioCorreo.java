/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

/**
 *
 * @author cesar
 */


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServicioCorreo {
    
    public static void enviarEmail(String destinatario, String asunto, String cuerpo) {
       
        final String correoAdmin = "juiopacp@gmail.com";
        final String passwordApp = "gtlz piys xgei azju".replace(" ", "");

       
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
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
            System.out.println("Correo enviado exitosamente a: " + destinatario);

        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            System.out.println("La operacion continua aunque el correo no se haya enviado.");
        }
    }
}

