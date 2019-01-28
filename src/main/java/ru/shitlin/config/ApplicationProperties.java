package ru.shitlin.config;

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

    public static class Schedule {

        private Invoice invoice;

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

        public Invoice getInvoice() {
            return invoice;
        }

        public void setInvoice(Invoice invoice) {
            this.invoice = invoice;
        }
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
