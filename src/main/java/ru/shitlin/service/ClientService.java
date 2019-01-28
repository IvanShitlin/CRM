package ru.shitlin.service;

import ru.shitlin.domain.Client;
import ru.shitlin.service.dto.ClientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Client.
 */
public interface ClientService {

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientDTO> findAll(Pageable pageable);


    /**
     * Get the "id" client.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ClientDTO> findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Save a client.
     *
     * @param amocrmId the Amocrm Contact ID
     * @return the persisted entity
     */
    Optional<Client> findByAmocrmId(Long amocrmId);

    /**
     * Save clients.
     *
     * @param clients the list of Clients
     */
    void saveAll(List<ClientDTO> clients);

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientDTO> search(String query, Pageable pageable);


    /**
     * Update and Save clients.
     * Use this for synchronization with Amocrm, when clients have no id, but can be found by amocrmId.
     *
     * @param clients the list of Clients
     */
    void updateAllByAmocrmId(List<ClientDTO> clients);
}
