package ro.fortsoft.monitor.alert.mail;

import ro.fortsoft.monitor.config.ConfigValue;

import java.util.List;

/**
 * @author sbalamaci
 */
class AlerterConfig {

    @ConfigValue("alerter.email.host")
    private String host;

    @ConfigValue("alerter.email.port")
    private Integer port;

    @ConfigValue("alerter.email.protocol")
    private String protocol;

    @ConfigValue("alerter.email.username")
    private String username;

    @ConfigValue("alerter.email.smtp.auth")
    private String smtpAuth;

    @ConfigValue("alerter.email.password")
    private String password;

    @ConfigValue("alerter.email.recipients")
    private List<String> recipients;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
}
