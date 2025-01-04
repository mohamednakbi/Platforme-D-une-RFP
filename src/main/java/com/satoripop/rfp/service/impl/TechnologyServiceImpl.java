package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.Technology;
import com.satoripop.rfp.repository.TechnologyRepository;
import com.satoripop.rfp.service.TechnologyService;
import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.mapper.TechnologyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.rfp.domain.Technology}.
 */
@Service
@Transactional
public class TechnologyServiceImpl implements TechnologyService {

    private final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);

    private final TechnologyRepository technologyRepository;

    private final TechnologyMapper technologyMapper;

    public TechnologyServiceImpl(TechnologyRepository technologyRepository, TechnologyMapper technologyMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public TechnologyDTO save(TechnologyDTO technologyDTO) {
        log.debug("Request to save Technology : {}", technologyDTO);
        Technology technology = technologyMapper.toEntity(technologyDTO);
        technology = technologyRepository.save(technology);
        return technologyMapper.toDto(technology);
    }

    @Override
    public TechnologyDTO update(TechnologyDTO technologyDTO) {
        log.debug("Request to update Technology : {}", technologyDTO);
        Technology technology = technologyMapper.toEntity(technologyDTO);
        technology = technologyRepository.save(technology);
        return technologyMapper.toDto(technology);
    }

    @Override
    public Optional<TechnologyDTO> partialUpdate(TechnologyDTO technologyDTO) {
        log.debug("Request to partially update Technology : {}", technologyDTO);

        return technologyRepository
            .findById(technologyDTO.getId())
            .map(existingTechnology -> {
                technologyMapper.partialUpdate(existingTechnology, technologyDTO);

                return existingTechnology;
            })
            .map(technologyRepository::save)
            .map(technologyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TechnologyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Technologies");
        return technologyRepository.findAll(pageable).map(technologyMapper::toDto);
    }

    public Page<TechnologyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return technologyRepository.findAllWithEagerRelationships(pageable).map(technologyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnologyDTO> findOne(Long id) {
        log.debug("Request to get Technology : {}", id);
        return technologyRepository.findOneWithEagerRelationships(id).map(technologyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Technology : {}", id);
        technologyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TechnologyDTO> searchByNamee(String name, Pageable pageable) {
        log.debug("Request to search Roles by name : {}", name);
        return technologyRepository.findByNameContainingIgnoreCase(name, pageable).map(technologyMapper::toDto);
    }
}
