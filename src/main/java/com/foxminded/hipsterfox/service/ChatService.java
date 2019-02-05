package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.domain.EmailAddress;
import com.foxminded.hipsterfox.domain.EmailMessage;
import com.foxminded.hipsterfox.repository.EmailAddressRepository;
import com.foxminded.hipsterfox.repository.EmailMessageRepository;
import com.foxminded.hipsterfox.service.dto.EmailMessageDTO;
import com.foxminded.hipsterfox.service.errors.EmailException;
import com.foxminded.hipsterfox.service.mapper.EmailMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(timeout = 5)
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final EmailMessageRepository emailMessageRepository;

    private final MailSessionConfigurationService mailSessionConfigurationService;

    private final EmailMessageMapper emailMessageMapper;

    private final EmailAddressRepository emailAddressRepository;

    private Session session;

    public ChatService(EmailMessageRepository emailMessageRepository, MailSessionConfigurationService mailSessionConfigurationService, EmailMessageMapper emailMessageMapper, EmailAddressRepository emailAddressRepository) {
        this.emailMessageRepository = emailMessageRepository;
        this.mailSessionConfigurationService = mailSessionConfigurationService;
        this.emailMessageMapper = emailMessageMapper;
        this.emailAddressRepository = emailAddressRepository;
    }

    @PostConstruct
    private void setSession() {
        session = mailSessionConfigurationService.getSession();
    }

    public void sendMessage(EmailMessageDTO emailMessageDTO) {
        EmailMessage emailMessage = emailMessageMapper.toEntity(emailMessageDTO);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            setMimeMessage(emailMessage, mimeMessage);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            String exceptionMessage = "Failed to send a message!";
            log.error(exceptionMessage, e);
            throw new EmailException(exceptionMessage, e);
        }
        saveSentMessageInDatabase(emailMessage);
    }

    private void setMimeMessage(EmailMessage emailMessage, MimeMessage mimeMessage) throws MessagingException {
        checkFromAddressIsValid(emailMessage);
        mimeMessage.setFrom(mailSessionConfigurationService.getUsername());
        mimeMessage.setRecipients(Message.RecipientType.TO, convertToInternetAddress(emailMessage.getTo()));
        mimeMessage.setRecipients(Message.RecipientType.CC, convertToInternetAddress(emailMessage.getCc()));
        mimeMessage.setSubject(emailMessage.getSubject());
        mimeMessage.setText(emailMessage.getBody());
    }

    private void checkFromAddressIsValid(EmailMessage emailMessage) {
        EmailAddress fromEmailAddress = emailMessage.getFrom();
        String actualFrom = mailSessionConfigurationService.getUsername();
        if (fromEmailAddress != null && !actualFrom.equals(fromEmailAddress.getAddress())) {
            String exceptionMessage = "Incorrect email address: Address must be null or equal to '" + mailSessionConfigurationService.getUsername() + "' !";
            log.error(exceptionMessage);
            throw new EmailException(exceptionMessage);
        }
        emailMessage.setFrom(new EmailAddress(actualFrom));
    }


    private Address[] convertToInternetAddress(Set<EmailAddress> emailAddress) {
        int emailAddressSize = emailAddress == null ? 0 : emailAddress.size();
        InternetAddress[] addresses = new InternetAddress[emailAddressSize];
        EmailAddress[] emailAddresses = emailAddress == null ? new EmailAddress[]{} : emailAddress.toArray(new EmailAddress[]{});
        for (int i = 0; i < emailAddressSize; i++) {
            try {
                addresses[i] = new InternetAddress(emailAddresses[i].getAddress());
            } catch (AddressException e) {
                String exceptionMessage = "Invalid email address!";
                log.error(exceptionMessage, e);
                throw new EmailException(exceptionMessage, e);
            }
        }
        return addresses;
    }

    private void saveSentMessageInDatabase(EmailMessage sentMessage) {
        EmailAddress fromEmailAddress = findOrCreateEmailAddress(sentMessage.getFrom());
        sentMessage.setTo(saveEmailAddresses(sentMessage.getTo()));
        sentMessage.setCc(saveEmailAddresses(sentMessage.getCc()));
        sentMessage.setFrom(fromEmailAddress);
        log.debug("Saving message: {}", sentMessage);
        emailMessageRepository.save(sentMessage);
    }

    private EmailAddress findOrCreateEmailAddress(EmailAddress address) {
        Optional<EmailAddress> databaseEmailAddress = emailAddressRepository.findByAddress(address.getAddress());
        if (databaseEmailAddress.isPresent()) {
            address = databaseEmailAddress.get();
        } else {
            emailAddressRepository.save(address);
        }
        return address;

    }

    private Set<EmailAddress> saveEmailAddresses(Set<EmailAddress> addresses) {
        Set<EmailAddress> addressSet = new HashSet<>();
        if (addresses != null) {
            for (EmailAddress emailAddress : addresses) {
                addressSet.add(findOrCreateEmailAddress(emailAddress));
            }
        }
        return addressSet;
    }

}
