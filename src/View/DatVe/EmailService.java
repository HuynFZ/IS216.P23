 
package View.DatVe;
import jakarta.mail.*;
    import jakarta.mail.internet.*;
    import jakarta.activation.DataHandler;
    import jakarta.activation.DataSource;
    import jakarta.activation.FileDataSource;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "nguyenvanmanhhuy1@gmail.com"; 
    // Mật khẩu ứng dụng 16 ký tự bạn đã tạo
    private static final String APP_PASSWORD = "rdcaqpexbxkandtu"; 
    
    public boolean sendEmailWithPdfAttachment(String recipientEmail, String subject, String messageBody, String pdfFilePath) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
    
        Session session = Session.getInstance(props, auth);
    
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            message.setSubject(subject, "UTF-8");
    
            // Tạo phần nội dung email (text/html)
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageBody, "text/html; charset=UTF-8");
    
            // Tạo phần đính kèm PDF
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfFilePath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("VeMayBay.pdf");  // Tên file đính kèm gửi đi
    
            // Ghép 2 phần (nội dung và file đính kèm)
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);
    
            message.setContent(multipart);
    
            // Gửi email
            Transport.send(message);
            System.out.println("Email gửi kèm PDF thành công!");
            return true;
    
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendOTPVerificationCode(String recipientEmail, String fullName, String otpCode) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            msg.setSubject("Mã xác thực đăng ký tài khoản", "UTF-8");

            String emailContent = String.format(
                "<h3>Xin chào %s,</h3>" +
                "<p>Bạn đang thực hiện đăng ký tài khoản. Mã xác thực (OTP) của bạn là:</p>" +
                "<h2 style='color:blue;'>%s</h2>" +
                "<p>Vui lòng nhập mã này vào màn hình xác thực để hoàn tất đăng ký.</p>" +
                "<p><i>Mã có hiệu lực trong 5 phút.</i></p>",
                fullName,
                otpCode
            );

            msg.setContent(emailContent, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
