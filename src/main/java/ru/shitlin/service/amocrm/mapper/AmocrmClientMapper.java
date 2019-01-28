package ru.shitlin.service.amocrm.mapper;

import ru.shitlin.service.amocrm.model.AmocrmContact;
import ru.shitlin.service.amocrm.model.AmocrmCustomField;
import ru.shitlin.service.dto.ClientDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Client and AmocrmContact.
 */
@Component
public class AmocrmClientMapper {

    public ClientDTO toClientDTO(AmocrmContact contact) {
        ClientDTO client = new ClientDTO();
        client.setAmocrmId(contact.getId());

        FullName fullName = convertFullName(contact.getName());
        client.setLastName(fullName.lastName == null ? "No Last Name" : fullName.lastName);
        client.setFirstName(fullName.firstName == null ? "No First Name" : fullName.firstName);
        client.setPatronymic(fullName.patronymic);

        Residence residence = parseResidence(findFieldValue(contact.getCustomFields(), 223451L));
        if (residence != null) {
            client.setCountry(residence.country);
            client.setCity(residence.city);
        }

        String email = findFieldValue(contact.getCustomFields(), 185329L);
        client.setEmail( email == null ? "noemail@mail.no" : email);

        client.setSkype(findFieldValue(contact.getCustomFields(), 185333L));
        client.setPhone(findFieldValue(contact.getCustomFields(), 185327L));
        client.setExperience(findFieldValue(contact.getCustomFields(), 224465L));

        return client;

    }

    public List<ClientDTO> toClientDTOList(List<AmocrmContact> contacts) {
        return contacts.stream().map(contact -> (toClientDTO(contact))).collect(Collectors.toList());
    }

    private String findFieldValue(List<AmocrmCustomField> customFields, Long fieldId) {
        return customFields.stream().filter(field -> Objects.equals(field.getId(), fieldId))
            .findFirst().map(item -> item.getValues().get(0).getValue()).orElse(null);

    }

    private class FullName {
        private String lastName;
        private String firstName;
        private String patronymic;
    }

    private FullName convertFullName(String fullName) {
        FullName result = new FullName();
        String[] parts = fullName.split("\\s+");
        if (parts.length > 0) {
            result.lastName = parts[0];
        }
        if (parts.length > 1) {
            result.firstName = parts[1];
        }
        if (parts.length > 2) {
            result.patronymic = parts[2];
        }
        return result;
    }

    private Residence parseResidence(String residence) {
        Residence result = null;
        if (residence != null) {
            result = new Residence();
            String[] residenceParts = residence.split("\\s+");
            result.country = residenceParts[0];
            if (residenceParts.length > 1) {
                result.city = residence.trim().substring(residenceParts[0].length() + 1);
            }
        }

        return result;
    }

    private class Residence {
        private String country;
        private String city;
    }
}
