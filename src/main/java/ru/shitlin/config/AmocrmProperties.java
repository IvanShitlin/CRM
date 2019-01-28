package ru.shitlin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "amocrm")
public class AmocrmProperties {

    private final Connection connection = new Connection();

    public static class Connection {

        private String protocol;
        private String host;
        private String port;
        private String login;
        private String apikey;
        private String authUrl;
        private String password;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBaseUrl() {
            if (port.equals("80")) {
                return protocol + "://" + host;
            }

            return protocol + "://" + host + ":" + port;
        }

        public String getFullAuthUrl() {
            return getBaseUrl() + authUrl;
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
