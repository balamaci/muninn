package ro.fortsoft.monitor.alert.mail;

import ro.fortsoft.monitor.config.ConfigValue;

/**
 * @author sbalamaci
 */
class AlerterConfig {

    @ConfigValue("mail.server.host")
    private String host;

    @ConfigValue("mail.server.port")
    private Integer port;

    @ConfigValue("mail.protocol")
    private String protocol;

    @ConfigValue("mail.username")
    private String username;

    @ConfigValue("mail.smtp.auth")
    private String smtpAuth;

    @ConfigValue("mail.password")
    private String password;

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
}
