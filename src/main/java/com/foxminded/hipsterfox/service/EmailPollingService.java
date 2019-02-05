package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.config.ApplicationProperties;
import com.foxminded.hipsterfox.config.Constants;
import com.foxminded.hipsterfox.domain.EmailAddress;
import com.foxminded.hipsterfox.domain.EmailMessage;
import com.foxminded.hipsterfox.domain.InboxPollingEvent;
import com.foxminded.hipsterfox.repository.EmailAddressRepository;
import com.foxminded.hipsterfox.repository.EmailMessageRepository;
import com.foxminded.hipsterfox.repository.InboxPollingEventRepository;
import com.foxminded.hipsterfox.service.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.search.*;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmailPollingService {

    private final Logger log = LoggerFactory.getLogger(EmailPollingService.class);

    private static final String INBOX = "INBOX";

    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(Constants.EMAIL_ADDRESS_REGEX,
        Pattern.CASE_INSENSITIVE);

    private final EmailAddressRepository emailAddressRepository;

    private final EmailMessageRepository emailMessageRepository;

    private final MailSessionConfigurationService mailSessionConfigurationService;

    private final InboxPollingEventRepository inboxPollingEventRepository;

    private final Session session;

    private final int messagesLimit;


    public EmailPollingService(ApplicationProperties applicationProperties, EmailAddressRepository emailAddressRepository,
                               EmailMessageRepository emailMessageRepository,
                               MailSessionConfigurationService mailSessionConfigurationService, InboxPollingEventRepository inboxPollingEventRepository) {

        this.emailAddressRepository = emailAddressRepository;
        this.emailMessageRepository = emailMessageRepository;
        this.mailSessionConfigurationService = mailSessionConfigurationService;
        this.inboxPollingEventRepository = inboxPollingEventRepository;

        session = mailSessionConfigurationService.getSession();
        messagesLimit = mailSessionConfigurationService.getMessageLimit();

    }

    public void pollInbox() {
        try  (Store store = session.getStore()) {
            store.connect();
            try (Folder inbox = store.getFolder(INBOX)) {
                inbox.open(Folder.READ_WRITE);
                saveNewMessages(inbox);
            }
        } catch (MessagingException | IOException e) {
            log.error("Failed to fetch messages!", e);
        }
    }

    private void saveNewMessages(Folder folder) throws MessagingException, IOException {
        InboxPollingEvent lastPollingEvent = inboxPollingEventRepository.
                findFirstByOrderByDateTimeDesc()
                .orElse(new InboxPollingEvent(DateUtil.EPOCH, 0));

        ZonedDateTime pollingStartDateTime = ZonedDateTime.now();
        
        Date currentPollingDate = DateUtil.toDate(pollingStartDateTime);
        Date lastPollingDate = DateUtil.toDate(lastPollingEvent.getDateTime());
        Date searchStartDate = DateUtil.toDate(lastPollingEvent.getDateTime()
                .minusDays(2)); // workaround to overcome IMAP SINCE search limitations and unknown timezone on the server
        Message[] messages = folder.search(new ReceivedDateTerm(ReceivedDateTerm.GE, searchStartDate));
        
        int savedMessagesCount = 0;
        int i = 0;
        
        while (i < messages.length && savedMessagesCount < messagesLimit) {
            
            if (messages[i].getReceivedDate().compareTo(lastPollingDate) >= 0) {
                saveMessage(messages[i]);
                savedMessagesCount++;
            }
            
            i++;
        } 

        InboxPollingEvent currentPollingEvent = new InboxPollingEvent(pollingStartDateTime, savedMessagesCount);
        inboxPollingEventRepository.save(currentPollingEvent);
    }

    private void saveMessage(Message message) throws MessagingException, IOException {
        Address[] addressesFrom = message.getFrom();
        Address[] addressesTo = message.getRecipients(Message.RecipientType.TO);
        Address[] addressesCc = message.getRecipients(Message.RecipientType.CC);

        Set<EmailAddress> toRecipients = extractAddresses(addressesTo);
        saveEmailAddresses(toRecipients);
        Set<EmailAddress> ccRecipients = extractAddresses(addressesCc);
        saveEmailAddresses(ccRecipients);

        EmailAddress from = null;

        if (addressesFrom != null && addressesFrom.length > 0) {
            from = findOrCreateEmailAddress(parseAddress(addressesFrom[0]));
            emailAddressRepository.save(from);
        }

        ZonedDateTime sentDateTime = DateUtil.toZonedDateTime(message.getSentDate());
        String subject = message.getSubject();

        EmailMessage emailMessage = new EmailMessage();

        emailMessage.setFrom(from);
        emailMessage.setTo(toRecipients);
        emailMessage.setCc(ccRecipients);
        emailMessage.setSentDateTime(sentDateTime);
        emailMessage.setSubject(subject);
        emailMessage.setBody(extractMessageBody(message));

        log.debug("Saving message: {}", emailMessage);
        emailMessageRepository.save(emailMessage);
    }

    private String extractMessageBody(Message message) throws MessagingException, IOException {
        String body = null;

        if (message.isMimeType(Constants.MULTIPART_MIME_TYPE)) {
            Multipart mp = (Multipart) message.getContent();

            for(int i = 0; i < mp.getCount(); i++) {
                BodyPart bodyPart = mp.getBodyPart(i);

                if (bodyPart.isMimeType(Constants.HTML_MIME_TYPE)) {
                    body = (String) bodyPart.getContent();
                    break;
                }

                if (body == null && bodyPart.isMimeType(Constants.PLAIN_TEXT_MIME_TYPE)) {
                    body = (String) bodyPart.getContent();
                }
            }
        }

        return body;
    }

    private Set<EmailAddress> extractAddresses(Address[] addresses) {
        Set<EmailAddress> result = new HashSet<>();

        if (addresses != null)
        {
            for (Address address : addresses) {
                EmailAddress emailAddress = findOrCreateEmailAddress(parseAddress(address));
                result.add(emailAddress);
            }
        }

        return result;
    }

    private void saveEmailAddresses(Set<EmailAddress> addresses) {
        for (EmailAddress emailAddress : addresses) {
            emailAddressRepository.save(emailAddress);
        }
    }

    /**
     * Extracts only address from email header, e.g. "John Smith &lt;js@domain.com&gt;" -> "js@domain.com"
     *
     * @param address {@link javax.mail.Address} object to parse
     * @return email address string
     */
    private String parseAddress(Address address) {
        String result = null;
        Matcher addressMatcher = EMAIL_ADDRESS_PATTERN.matcher(address.toString());

        if (addressMatcher.find()) {
            result = addressMatcher.group();
        }

        return result;
    }

    private EmailAddress findOrCreateEmailAddress(String address) {
        return emailAddressRepository.findByAddress(address)
            .orElse(new EmailAddress(address));
    }

}
