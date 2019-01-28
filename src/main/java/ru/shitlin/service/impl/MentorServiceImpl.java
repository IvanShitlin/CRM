package ru.shitlin.service.impl;

import ru.shitlin.service.MentorService;
import ru.shitlin.domain.Mentor;
import ru.shitlin.repository.MentorRepository;
import ru.shitlin.repository.view.MentorViewRepository;
import ru.shitlin.service.dto.MentorDTO;
import ru.shitlin.service.view.dto.MentorViewDTO;
import ru.shitlin.service.mapper.MentorMapper;
import ru.shitlin.service.view.mapper.MentorViewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Mentor.
 */
@Service
@Transactional
public class MentorServiceImpl implements MentorService {

    private final Logger log = LoggerFactory.getLogger(MentorServiceImpl.class);

    private final MentorRepository mentorRepository;

    private final MentorViewRepository mentorViewRepository;

    private final MentorMapper mentorMapper;

    private final MentorViewMapper mentorViewMapper;

    public MentorServiceImpl(MentorRepository mentorRepository, MentorViewRepository mentorViewRepository,
                             MentorMapper mentorMapper, MentorViewMapper mentorViewMapper) {
        this.mentorRepository = mentorRepository;
        this.mentorViewRepository = mentorViewRepository;
        this.mentorMapper = mentorMapper;
        this.mentorViewMapper = mentorViewMapper;
    }

    /**
     * Save a mentor.
     *
     * @param mentorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MentorDTO save(MentorDTO mentorDTO) {
        log.debug("Request to save Mentor : {}", mentorDTO);
        Mentor mentor = mentorMapper.toEntity(mentorDTO);
        mentor = mentorRepository.save(mentor);
        return mentorMapper.toDto(mentor);
    }

    /**
     * Get all the mentors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MentorViewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mentors");
        return mentorViewRepository.findAll(pageable)
            .map(mentorViewMapper::toDto);
    }

    /**
     * Get one mentor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MentorViewDTO> findOne(Long id) {
        log.debug("Request to get Mentor : {}", id);
        return mentorViewRepository.findById(id)
            .map(mentorViewMapper::toDto);
    }

    /**
     * Delete the mentor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mentor : {}", id);
        mentorRepository.deleteById(id);
    }
}
