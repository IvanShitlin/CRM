package com.foxminded.hipsterfox.service.impl;

import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.repository.ClientRepository;
import com.foxminded.hipsterfox.repository.search.ClientSearchRepository;
import com.foxminded.hipsterfox.service.ClientService;
import com.foxminded.hipsterfox.service.dto.ClientDTO;
import com.foxminded.hipsterfox.service.mapper.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final ClientSearchRepository clientSearchRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientSearchRepository = clientSearchRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    @Override
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        client = clientRepository.save(client);
        clientSearchRepository.save(client);
        return client;
    }

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        ClientDTO result = clientMapper.toDto(client);
        clientSearchRepository.save(client);
        return result;
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable)
            .map(clientMapper::toDto);
    }


    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id)
            .map(clientMapper::toDto);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
        clientSearchRepository.deleteById(id);
    }

    @Override
    public Optional<Client> findByAmocrmId(Long amocrmId) {
        return clientRepository.findByAmocrmId(amocrmId);
    }

    @Override
    public void saveAll(List<ClientDTO> clients) {
        clientRepository.saveAll(clientMapper.toEntity(clients));
    }

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clients for query {}", query);
        return clientSearchRepository.search(queryStringQuery(query), pageable)
            .map(clientMapper::toDto);
    }

    @Override
    public void updateAllByAmocrmId(List<ClientDTO> clients) {
        for (ClientDTO dto : clients) {
            findByAmocrmId(dto.getAmocrmId()).ifPresent(client -> dto.setId(client.getId()));
        }
        saveAll(clients);
    }
}
