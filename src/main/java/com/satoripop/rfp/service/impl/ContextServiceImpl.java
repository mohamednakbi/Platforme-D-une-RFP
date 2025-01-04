package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.Context;
import com.satoripop.rfp.repository.ContextRepository;
import com.satoripop.rfp.service.ContextService;
import com.satoripop.rfp.service.dto.ContextDTO;
import com.satoripop.rfp.service.mapper.ContextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.rfp.domain.Context}.
 */
@Service
@Transactional
public class ContextServiceImpl implements ContextService {

    private final Logger log = LoggerFactory.getLogger(ContextServiceImpl.class);

    private final ContextRepository contextRepository;

    private final ContextMapper contextMapper;

    public ContextServiceImpl(ContextRepository contextRepository, ContextMapper contextMapper) {
        this.contextRepository = contextRepository;
        this.contextMapper = contextMapper;
    }

    @Override
    public ContextDTO save(ContextDTO contextDTO) {
        log.debug("Request to save Context : {}", contextDTO);
        Context context = contextMapper.toEntity(contextDTO);
        context = contextRepository.save(context);
        return contextMapper.toDto(context);
    }

    @Override
    public ContextDTO update(ContextDTO contextDTO) {
        log.debug("Request to update Context : {}", contextDTO);
        Context context = contextMapper.toEntity(contextDTO);
        context = contextRepository.save(context);
        return contextMapper.toDto(context);
    }

    @Override
    public Optional<ContextDTO> partialUpdate(ContextDTO contextDTO) {
        log.debug("Request to partially update Context : {}", contextDTO);

        return contextRepository
            .findById(contextDTO.getId())
            .map(existingContext -> {
                contextMapper.partialUpdate(existingContext, contextDTO);

                return existingContext;
            })
            .map(contextRepository::save)
            .map(contextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contexts");
        return contextRepository.findAll(pageable).map(contextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContextDTO> findOne(Long id) {
        log.debug("Request to get Context : {}", id);
        return contextRepository.findById(id).map(contextMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Context : {}", id);
        contextRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContextDTO> searchByNamee(String name, Pageable pageable) {
        log.debug("Request to search Roles by name : {}", name);
        return contextRepository.findByNameContainingIgnoreCase(name, pageable).map(contextMapper::toDto);
    }
}
