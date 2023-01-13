package demo.api.core.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    private InternetAddress[] listToArray(List<String> list, String charSet) {
        List<InternetAddress> internetAddressList = new ArrayList<>();
        list.stream().forEach(s->{
            try {
                internetAddressList.add(new InternetAddress(s, null, charSet));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        return internetAddressList.toArray(new InternetAddress[internetAddressList.size()]);
    }

    public void sendMail(MailVo mailVo) {
        String charSet = "UTF-8";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            InternetAddress[] toAddress = listToArray(mailVo.getToAddressList(), charSet);
            InternetAddress[] ccAddress = listToArray(mailVo.getCcAddressList(), charSet);
            InternetAddress[] bccAddress = listToArray(mailVo.getBccAddressList(), charSet);

            mimeMessageHelper.setSubject(mailVo.getSubject());
            mimeMessageHelper.setText(mailVo.getContent());
            mimeMessageHelper.setFrom(new InternetAddress(mailVo.getFromAddress(), null, charSet));
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setCc(ccAddress);
            mimeMessageHelper.setBcc(bccAddress);

            if (!CollectionUtils.isEmpty(mailVo.getAttachFileList())) {
                for (AttachFileVo attachFileVo : mailVo.getAttachFileList()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachFileVo.getRealFileName()));
                    mimeMessageHelper.addAttachment(MimeUtility.encodeText(attachFileVo.getAttachFileName(), charSet, "B"), fileSystemResource);
                }
            }

            javaMailSender.send(mimeMessage);

            log.info("The mail was sent normally.");

        } catch (Exception e) {

            log.error("The mail couldn't send.");

        }
    }


}
