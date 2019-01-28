package ru.shitlin.service.view.impl;

import ru.shitlin.repository.view.DebtorViewRepository;
import ru.shitlin.service.view.dto.DebtorViewDTO;
import ru.shitlin.service.view.mapper.DebtorViewMapper;
import ru.shitlin.service.view.service.DebtorViewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DebtorViewServiceImpl implements DebtorViewService {

    private final Logger log = LoggerFactory.getLogger(DebtorViewServiceImpl.class);

    private final DebtorViewRepository debtorViewRepository;

    private final DebtorViewMapper debtorViewMapper;

    public DebtorViewServiceImpl(DebtorViewRepository debtorViewRepository, DebtorViewMapper debtorViewMapper) {
        this.debtorViewRepository = debtorViewRepository;
        this.debtorViewMapper = debtorViewMapper;
    }

    @Override
    public Page<DebtorViewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mentors");
        return debtorViewRepository
            .findAll(pageable)
            .map(debtorViewMapper::toDto);

    }
}
