package com.foxminded.hipsterfox.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Hipsterfox.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Schedule schedule;
    private MailChat mailChat;

    public static class Schedule {

        private Invoice invoice;
        private MailPolling mailPolling;

        public static class Invoice {
            private String creationCron;
            private int issueDaysBeforePaymentDate;

            public String getCreationCron() {
                return creationCron;
            }

            public void setCreationCron(String creationCron) {
                this.creationCron = creationCron;
            }

            public int getIssueDaysBeforePaymentDate() {
                return issueDaysBeforePaymentDate;
            }

            public void setIssueDaysBeforePaymentDate(int issueDaysBeforePaymentDate) {
                this.issueDaysBeforePaymentDate = issueDaysBeforePaymentDate;
            }
        }

        public static class MailPolling {
            private String cron;

            public String getCron() {
                return cron;
            }

            public void setCron(String cron) {
                this.cron = cron;
            }
        }

        public Invoice getInvoice() {
            return invoice;
        }

        public void setInvoice(Invoice invoice) {
            this.invoice = invoice;
        }

        public MailPolling getMailPolling() {
            return mailPolling;
        }

        public void setMailPolling(MailPolling mailPolling) {
            this.mailPolling = mailPolling;
        }
    }

    public static class MailChat {
        private MailServer mailServer;

        private int messagesLimit;

        public static class MailServer {
            private Protocol protocol;
            private String username;
            private String password;

            public static class Protocol {
                private IMAPS imaps;
                private SMTP smtp;

                public static class IMAPS {
                    private String host;
                    private String port;
                    private String timeout;

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

                    public String getTimeout() {
                        return timeout;
                    }

                    public void setTimeout(String timeout) {
                        this.timeout = timeout;
                    }

                }

                public static class SMTP {
                    private String host;
                    private String port;
                    private String timeout;

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

                    public String getTimeout() {
                        return timeout;
                    }

                    public void setTimeout(String timeout) {
                        this.timeout = timeout;
                    }

                }

                public IMAPS getImaps() {
                    return imaps;
                }

                public void setImaps(IMAPS imaps) {
                    this.imaps = imaps;
                }

                public SMTP getSmtp() {
                    return smtp;
                }

                public void setSmtp(SMTP smtp) {
                    this.smtp = smtp;
                }

            }

            public Protocol getProtocol() {
                return protocol;
            }

            public void setProtocol(Protocol protocol) {
                this.protocol = protocol;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public MailServer getMailServer() {
            return mailServer;
        }

        public void setMailServer(MailServer mailServer) {
            this.mailServer = mailServer;
        }

        public int getMessagesLimit() {
            return messagesLimit;
        }

        public void setMessagesLimit(int messagesLimit) {
            this.messagesLimit = messagesLimit;
        }
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public MailChat getMailChat() {
        return mailChat;
    }

    public void setMailChat(MailChat mailChat) {
        this.mailChat = mailChat;
    }
}
