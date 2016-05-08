package ro.fortsoft.monitor.alert.mail;

import com.typesafe.config.Config;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import ro.fortsoft.monitor.alert.BaseAlerter;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.config.ConfigInjector;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * @author sbalamaci
 */
public class MailAlerter extends BaseAlerter {


    private JavaMailSender mailSender;

    public MailAlerter(Config config) {
        super(config);

        AlerterConfig alerterConfig = new AlerterConfig();
        new ConfigInjector(alerterConfig, config).inject();
        mailSender = buildMailSender(alerterConfig);
    }

    @Override
    public String getName() {
        return "alerter.email";
    }

    @Override
    public void alert(BaseRule rule, String content) throws Exception {
        Config ruleConfig = rule.getConfig();
        List<String> recipients = ruleConfig.getStringList("alerter.email.recipients");

        String from = "";
        String subject = "";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, false, "utf-8");

        mailMessage.setFrom(from);

        mailMessage.setTo(recipients.toArray(new String[recipients.size()]));
        mailMessage.setSubject(subject);
        mimeMessage.setContent(content, "text/html");
    }

    private JavaMailSender buildMailSender(AlerterConfig config) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", config.getSmtpAuth());
        mailProperties.put("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(config.getHost());
        mailSender.setPort(config.getPort());
        mailSender.setProtocol(config.getProtocol());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());
        return mailSender;
    }

}
